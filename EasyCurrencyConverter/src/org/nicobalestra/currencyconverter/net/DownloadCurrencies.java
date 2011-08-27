package org.nicobalestra.currencyconverter.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.nicobalestra.currencyconverter.Currency;
import org.nicobalestra.currencyconverter.data.CurrenciesDB;
import org.nicobalestra.currencyconverter.data.CurrencyStorageHelper;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadCurrencies extends AsyncTask<Void, Void, Currency[]> {

	private OnDownloadCompleted callback;
	
	private static final String TAG = "DownloadCurrencies";
	
	private final HttpClient client = new DefaultHttpClient();
	
	XmlPullParser parser;
	
	public DownloadCurrencies(OnDownloadCompleted mCallback){
		this.callback = mCallback;
	}
	
	@Override
	protected Currency[] doInBackground(Void... params) {
		List<Currency> toReturn = new ArrayList<Currency>();
		
		try{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(false);
			this.parser = factory.newPullParser();
		}
		catch (XmlPullParserException pe){
			//TODO NICO Handle this exception
			Log.e(TAG, "Error while parsing the XML of the currency exchange..");
		}
		CurrenciesDB db = this.callback.getCurrenciesDBInstance();
		Cursor cur = db.getConvertedCurrencies();
		Currency target = db.getTargetCurrency();
		
		 try {
		      InetAddress i = InetAddress.getByName("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate");
		    } catch (UnknownHostException e1) {
		      Log.d(TAG, "DEBUG: " + e1.getMessage());
		    }
		
		//For each currency...
		while (cur.moveToNext()){
			String codeTo = cur.getString(cur.getColumnIndex(CurrencyStorageHelper.COL_CURRENCY_CODE));
			
			Log.d(TAG, "Setting up http parameters...");
			List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
			
			urlParams.add(new BasicNameValuePair("ToCurrency", codeTo));
			urlParams.add(new BasicNameValuePair("FromCurrency", target.getCode()));
			String paramString = URLEncodedUtils.format(urlParams, "utf-8");
			HttpGet get = new HttpGet("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?" + paramString);
			
			try{
				Log.d(TAG, "Executing get request.." + get.getURI());
				HttpResponse response = client.execute(get);
				double val = getValueFromHTTPResponse(response);
				Log.d(TAG, "Conversion: 1 " + target.getCode() + " = " + val + " " + codeTo);
				
				toReturn.add(new Currency(codeTo, val));
			}
			catch (ClientProtocolException cpe){
				Log.e("DownloadCurrencies", "HTTP Error", cpe);
				
			}
			catch (IOException ioe){
				Log.e(TAG, "Socket exception", ioe);
			}
			catch(XmlPullParserException pe){
				//TODO NICO Handle this exception
				Log.e(TAG, "Error while parsing the returned XML", pe);
			}

		}
		
		if (toReturn.isEmpty())
			return null;
		
		try{
			Object[] test = (Object[])toReturn.toArray();
			
			Log.d(TAG, "This is a test.. " + test.getClass().getName());
		}
		catch(RuntimeException re){
			Log.e(TAG, "Ok, this is a cast exception", re);
		}
		return (Currency[])toReturn.toArray();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(Currency[] result) {
		this.callback.onComplete(result);

	}

	private double getValueFromHTTPResponse(HttpResponse response) throws XmlPullParserException, IOException {
		double toReturn = 0;
		try{
			parser.setInput(new InputStreamReader(response.getEntity().getContent()));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT){
				if (eventType == XmlPullParser.TEXT){
					//TOD NICO Strengthen this check
					String value = parser.getText();
					toReturn = Double.parseDouble(value);
				}
				eventType = parser.next();
			}
		}
		catch (Exception e){
			Log.e(TAG, "Error while getting the currency value from the returned XML", e);
		}
		
		return toReturn;
	}
	
	public interface OnDownloadCompleted{
		public Context getContext();
		public void onComplete(Currency[] result);
		
		public CurrenciesDB getCurrenciesDBInstance();
	}

	
}
