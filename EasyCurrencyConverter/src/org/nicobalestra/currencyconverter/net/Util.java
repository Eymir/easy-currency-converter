package org.nicobalestra.currencyconverter.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Util {

	private static final String TAG = "Util";
	
	private Context ctx;
	private ConnectivityManager cm;
	
	public Util(Context mContext){
		this.ctx = mContext;
		
		this.cm = (ConnectivityManager)this.ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public boolean isConnectedToTheInternet(){
		
		NetworkInfo connection = this.cm.getActiveNetworkInfo();
		
		Log.d(TAG, "Is there any active connection? " + String.valueOf(connection != null));
		return connection != null;


	}
}
