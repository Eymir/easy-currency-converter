package org.nicobalestra.currencyconverter;

import org.nicobalestra.currencyconverter.data.CurrencyStorageHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CurrencyDownloadAdapter extends SimpleCursorAdapter {
	
	private Context ctx;

	private static final int layout = R.layout.currencies_list;
	
	public CurrencyDownloadAdapter(Context context, Cursor c) {
		super(context, 
			  CurrencyDownloadAdapter.layout, 
			  c, 
			  new String[] {CurrencyStorageHelper.COL_CURRENCY_FULL_NAME,
							CurrencyStorageHelper.COL_CURRENCY_VALUE}, 
			  new int[] {R.id.currencyAbbr,
						 R.id.currencyValue});
		this.ctx = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(CurrencyDownloadAdapter.layout, parent, false);

		Currency currency = new Currency(cursor);
		TextView abbr = (TextView) layout.findViewById(R.id.currencyAbbr);
		if (abbr != null && currency.getName() != ""){
			abbr.setText(currency.getName() + ": " + currency.getFullName());
		}
		
		ImageView icon = (ImageView)layout.findViewById(R.id.currencyFlag);
		if (currency.getImgResourceID() != 0 && icon != null){
			icon.setImageResource(currency.getImgResourceID());
		}
		
		TextView val = (TextView)layout.findViewById(R.id.currencyValue);
		if (val != null){
			val.setText(String.valueOf(currency.getValue()));
		}
		
		return layout;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		Currency currency = new Currency(cursor);
		TextView abbr = (TextView) view.findViewById(R.id.currencyAbbr);
		if (abbr != null && currency.getName() != ""){
			abbr.setText(currency.getName() + ": " + currency.getFullName());
		}
		
		ImageView icon = (ImageView)view.findViewById(R.id.currencyFlag);
		if (currency.getImgResourceID() != 0 && icon != null){
			icon.setImageResource(currency.getImgResourceID());
		}
		
		TextView val = (TextView)view.findViewById(R.id.currencyValue);
		if (val != null){
			val.setText(String.valueOf(currency.getValue()));
		}
		
	}
	
	
}
