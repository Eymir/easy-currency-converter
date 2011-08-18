package org.nicobalestra.currencyconverter.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.nicobalestra.currencyconverter.Currency;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadCurrencies extends AsyncTask<Void, Void, Currency[]> {

	private Context ctx;
	
	private static final String TAG = "DownloadCurrencies";
	
	private final HttpClient client = new DefaultHttpClient();
	private final HttpGet get = new HttpGet("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=EUR");

	public DownloadCurrencies(Context mCtx){
		this.ctx = mCtx;
	}
	
	@Override
	protected Currency[] doInBackground(Void... params) {

		
		HttpParams urlParams = new BasicHttpParams();
		urlParams.setParameter("ToCurrency", "EUR");
		urlParams.setParameter("FromCurrency", "USD");
        
		get.setParams(urlParams);
		try{
			HttpResponse response = client.execute(get);
		}
		catch (ClientProtocolException cpe){
			Log.e("DownloadCurrencies", "HTTP Error", cpe);
		}
		catch (IOException ioe){
			Log.e(TAG, "Socket exception", ioe);
		}
		
		
		SharedPreferences preferences = this.ctx.getSharedPreferences("PreferredCurrencies", 0);
		preferences.getAll();
		return null;
	}
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
	
	@Override
	protected void onPostExecute(Currency[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
}
