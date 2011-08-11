package org.nicobalestra.currencyconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;

public class EasyCurrencyConverterActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Currency> data = getData();
        
        setListAdapter(new CurrencyDownloadAdapter(this, data));
    }
	
	public List<Currency>getData(){
		
		List<Currency> data = new ArrayList<Currency>();
		
		Map<String, Integer> staticData = Currency.getCurrenciesDefinition();	
		
		for (String currencyName : staticData.keySet() ){
			Currency curr = new Currency(currencyName, Math.random());
			data.add(curr);
		}
		
		return data;
	}
}