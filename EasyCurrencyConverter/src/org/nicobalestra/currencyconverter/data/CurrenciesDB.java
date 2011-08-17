package org.nicobalestra.currencyconverter.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CurrenciesDB {
	
	private Context ctx;
	private CurrencyStorageHelper dbHelper;
	private SQLiteDatabase db;
	
	public CurrenciesDB(Context mCtx) {
		this.ctx = mCtx;
	}
	
	public CurrenciesDB open(){
		this.dbHelper = new CurrencyStorageHelper(this.ctx);
		this.db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		this.dbHelper.close();
	}
	
	public Cursor getLatestCurrencies(){
		return db.rawQuery("SELECT " + CurrencyStorageHelper.COL_ID 
						      + " ," + CurrencyStorageHelper.COL_CURRENCY_CODE
						      + " ," + CurrencyStorageHelper.COL_CURRENCY_FULL_NAME
						      + " ," + CurrencyStorageHelper.COL_CURRENCY_VALUE
						      + " ," + CurrencyStorageHelper.COL_LAST_UPDATE
						      + " ," + CurrencyStorageHelper.COL_LAST_DOWNLOADED
						      + " FROM " + CurrencyStorageHelper.DATABASE_NAME 
						      + " WHERE date(" + CurrencyStorageHelper.COL_LAST_DOWNLOADED + ") = " 
						      +	"(SELECT MAX(date(" + CurrencyStorageHelper.COL_LAST_DOWNLOADED + ") "
						      + " FROM " + CurrencyStorageHelper.DATABASE_NAME , null);
	}
}
