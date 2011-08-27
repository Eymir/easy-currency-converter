package org.nicobalestra.currencyconverter;

import org.nicobalestra.currencyconverter.data.CurrenciesDB;
import org.nicobalestra.currencyconverter.net.DownloadCurrencies;
import org.nicobalestra.currencyconverter.net.Util;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EasyCurrencyConverterActivity extends ListActivity implements DownloadCurrencies.OnDownloadCompleted {
    private static final String TAG = EasyCurrencyConverterActivity.class.getName();
	/** Called when the activity is first created. */
	private final int DIALOG_HELP = 100;
	private final int DIALOG_ERROR_DOWNLOAD = 101;
	
	CurrenciesDB currenciesDB;
	ProgressDialog downloadingDialog; 

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getListView().setDividerHeight(2);
        setContentView(R.layout.main_currency_list);
        //Initiate the DB. Create it if doesn't exists and set some default currencies.
        currenciesDB = new CurrenciesDB(this);
        currenciesDB.open();
        
        
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
		Log.d(TAG, "CrateDialog: " + id);
		
		switch (id){
			case DIALOG_HELP :
				dialog = showHelp();
				break;
			case DIALOG_ERROR_DOWNLOAD :
				dialog = showErrorDownload();
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

	private Dialog showErrorDownload(){
		Log.d(TAG, "Showing error of downloading... ");
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_download_error);
		
		dialog.setTitle("Download Error");

		return dialog;
	}

	
	private void fillList(){
		//Check if there's any internet connection...
		Util netUtil = new Util(this);
		if (netUtil.isConnectedToTheInternet()){
			//TODO Nico - Show a progress dialog
			DownloadCurrencies download = new DownloadCurrencies(this);
			download.execute(new Void[]{});
		}
		
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (currenciesDB != null){
			currenciesDB.close();
		}
		
	}
	
	@Override
	public Context getContext() {
		return this;
	}
	
	@Override
	public void onComplete(Currency[] result) {
		
		if (result == null || result.length == 0){
			//show an error
			showDialog(DIALOG_ERROR_DOWNLOAD);
			return;
		}

		currenciesDB.addDownlodadedCurrencies(result);
		
		
	}
	
	@Override
	public CurrenciesDB getCurrenciesDBInstance() {
		// TODO Auto-generated method stub
		return this.currenciesDB;
	}
}