package org.nicobalestra.currencyconverter.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nicobalestra.currencyconverter.Currency;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrencyUserPreferences {

	private static final String DEFAULT_CURRENCY_PREF = "currency_onload";
	private Context ctx;
	private SharedPreferences sPref;
	
	public CurrencyUserPreferences(Context mCtx){
		this.ctx = mCtx;
		this.sPref = this.ctx.getSharedPreferences(DEFAULT_CURRENCY_PREF, 0);
	}
	
	public Currency getTargetCurrency(){
		//Get the default target currency. If the property
		//doesn't exist the target currency will be EUR
		
		//Potentially we could use the geolocalization to get the 
		//user's location currency
		String defaultCurr = this.sPref.getString("TARGET_CURRENCY", "EUR");
		
		Currency curr = new Currency(defaultCurr, 1);
		
		return curr;
	}
	
	public List<Currency> getAllDefaultCurrency() {
		
		
		Map<String, String> all = (Map<String, String>)this.sPref.getAll();
		Currency targetCurr = this.getTargetCurrency();
		List<Currency> allToReturn = new ArrayList<Currency>();
		
		for (String key : all.keySet() ){
			//Skip the target currency...
			if (key == targetCurr.getCode()){
				continue;
			}
			
			double value = this.sPref.getFloat(key + "_value", 0);
			String fullName = this.sPref.getString(key + "_fullName", "Name not found");
			
			Currency curr = new Currency(key, fullName,  value);
			
			allToReturn.add(curr);
		}
		
		return allToReturn;
		
	}
	
}
