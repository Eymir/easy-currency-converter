package org.nicobalestra.currencyconverter;

import org.nicobalestra.currencyconverter.data.CurrenciesDB;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EasyCurrencyConverterActivity extends ListActivity {
    /** Called when the activity is first created. */
	private final int DIALOG_HELP = 100;
	
	CurrenciesDB currenciesDB;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getListView().setDividerHeight(2);
        setContentView(R.layout.main_currency_list);
        
        fillList();
        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

	private void fillList(){
		this.currenciesDB= new CurrenciesDB(this);
		currenciesDB.open();
		Cursor c = currenciesDB.getLatestCurrencies();
		
		if (c.getCount() <= 0){
			//Start the downloading of the currencies in a new thread...
		}
		
		startManagingCursor(c);
		
		CurrencyDownloadAdapter adapter = new CurrencyDownloadAdapter(this, c);
		setListAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		currenciesDB.close();
		
	}
}