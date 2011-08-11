package org.nicobalestra.currencyconverter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrencyDownloadAdapter extends ArrayAdapter<Currency> {
	
	private Activity ctx;
	private List<Currency> data;
	
	public CurrencyDownloadAdapter(Activity ctx, List<Currency> data){
		super (ctx, R.layout.currencies_list, data);
		
		this.ctx = ctx;
		this.data = data;
	}
	
	static class ViewHolder{
		ImageView image;
		TextView currencyName;
		TextView currencyValue;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View toReturn = null;
		ViewHolder holder;
		
			if (convertView == null){
				LayoutInflater inflater = this.ctx.getLayoutInflater();
				toReturn = inflater.inflate(R.layout.currencies_list, null);
				
				holder = new ViewHolder();
				holder.image = (ImageView)toReturn.findViewById(R.id.currencyFlag);
				
				holder.currencyName = (TextView)toReturn.findViewById(R.id.currencyAbbr);
				
				holder.currencyValue = (TextView)toReturn.findViewById(R.id.currencyValue);
				
				toReturn.setTag(holder);
				
			}
			else {
				toReturn = convertView;
			}
			
			Currency currency = data.get(position);
			
			holder = (ViewHolder)toReturn.getTag();
			holder.currencyName.setText(currency.getName());
			holder.currencyValue.setText(String.valueOf(currency.getValue()));
			holder.image.setImageResource(currency.getImgResourceID());
			
		
		return toReturn;
	}
	
}
