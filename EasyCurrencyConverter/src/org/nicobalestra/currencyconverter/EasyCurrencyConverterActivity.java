package org.nicobalestra.currencyconverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EasyCurrencyConverterActivity extends ListActivity {
    /** Called when the activity is first created. */
	private final int DIALOG_HELP = 100;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Currency> data = getData();
        
        setListAdapter(new CurrencyDownloadAdapter(this, data));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()){
		case R.id.menu_refresh:
			Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_donate:
			Toast.makeText(this, "Donate", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_help:
			showDialog(DIALOG_HELP);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		Log.d("Activity", "CrateDialog: " + id);
		
		switch (id){
			case DIALOG_HELP :
				dialog = showHelp();
				break;
			default:
				dialog = null;
		}
		
		return dialog;
	}
	
	
	private Dialog showHelp(){
		Log.d("ShowHelp", "Called.. ");
		Context ctx = getApplicationContext();
		Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.dialog_layout);
		
		dialog.setTitle("About this...");
		Log.d("ShowHelp", "Returning the dialog: " + dialog);
		return dialog;
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