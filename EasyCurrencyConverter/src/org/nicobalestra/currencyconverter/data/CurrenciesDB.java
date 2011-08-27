package org.nicobalestra.currencyconverter.data;

import java.util.Date;

import org.nicobalestra.currencyconverter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CurrenciesDB {
	private static final String TAG = "CurrenciesDB";  
	private enum enumQueryCurrentCurrencies{
		ALL,
		TARGET,
		CONVERTED
	}
	
	private Context ctx;
	private CurrencyStorageHelper dbHelper;
	private SQLiteDatabase db;
	
	public CurrenciesDB(Context mCtx) {
		this.ctx = mCtx;
	}
	
	public CurrenciesDB open() throws SQLException {
		try{
		this.dbHelper = new CurrencyStorageHelper(this.ctx);
		this.db = dbHelper.getWritableDatabase();
		}
		catch (Exception e){
			Log.e(TAG, "ERRORRRRRRRR", e);
		}
		return this;
	}
	
	public void close(){
		this.dbHelper.close();
	}
	
	/**
	 * Retrieve the currency which will be used as reference to get 
	 * currencies exchange.
	 * There is only 1 record with the flag "Is Target" set to true in the default_currencies table 
	 * @return
	 */
	public Currency getTargetCurrency(){
		Cursor cur = queryCurrentCurrencies(enumQueryCurrentCurrencies.TARGET);
		Currency toReturn = new Currency();

		if (!cur.moveToFirst()){
			//If no target currency has been found into the database, 
			//use the US Dollar as a default currency. 
			//TODO NICO This should use the geolocalization to get the user's local currency 
			toReturn.setCode("USD");
			toReturn.setValue(1);
			toReturn.setFullName("AmericanDollar");
		}
		else{
			toReturn.setCode(cur.getString(cur.getColumnIndex(CurrencyStorageHelper.COL_CURRENCY_CODE)));
			toReturn.setFullName(cur.getString(cur.getColumnIndex(CurrencyStorageHelper.COL_DEFINITION_FULL_NAME)));
			toReturn.setValue(cur.getDouble(cur.getColumnIndex(CurrencyStorageHelper.COL_CURRENCY_VALUE)));
		}
		
		return toReturn;
	}
	
	private Cursor queryCurrentCurrencies(enumQueryCurrentCurrencies rule){
		
		String SQL = "SELECT v." + CurrencyStorageHelper.COL_ID 
				 + ", v." + CurrencyStorageHelper.COL_CURRENCY_CODE
				 + ", d." + CurrencyStorageHelper.COL_DEFINITION_FULL_NAME
				 + ", v." + CurrencyStorageHelper.COL_CURRENCY_VALUE 
				 + " FROM " + CurrencyStorageHelper.TABLE_DEFAULT_CURRENCIES + " v " 
				 + " JOIN " + CurrencyStorageHelper.TABLE_CURRENCY_DEFINITION + " d "
				 + " ON v." + CurrencyStorageHelper.COL_CURRENCY_CODE
				 + " =  d." + CurrencyStorageHelper.COL_DEFINITION_CODE;
				 
		if (rule.compareTo(enumQueryCurrentCurrencies.TARGET) == 0){
			SQL += " WHERE v." + CurrencyStorageHelper.COL_IS_TARGET_CURRENCY + " = 1";
		} 
		else if (rule.compareTo(enumQueryCurrentCurrencies.CONVERTED) == 0){
			SQL += " WHERE v." + CurrencyStorageHelper.COL_IS_TARGET_CURRENCY + " = 0";
		}
		
		SQL += ";";
		
		return this.db.rawQuery(SQL, new String[] {});
	}
	
	/**
	 * Retrieve the cursor containing all the converted currencies for the user 
	 * related to the target currency.
	 * The converted currencies are the currencies shown to the user once the application
	 * starts up. They basically are the last downloaded currencies.
	 * Every time the user refresh the currencies value, the "default_currencies" 
	 * table gets repopulated.
	 * @return
	 */
	public Cursor getConvertedCurrencies(){
		return queryCurrentCurrencies(enumQueryCurrentCurrencies.CONVERTED);
	}

	/**
	 * Retrieve the cursor containing all the converted currencies for the user 
	 * related to the target currency.
	 * The converted currencies are the currencies shown to the user once the application
	 * starts up. They basically are the last downloaded currencies.
	 * Every time the user refresh the currencies value, the "default_currencies" 
	 * table gets repopulated.
	 * @return
	 */
	public Cursor getAllCurrentCurrencies(){
		
		return queryCurrentCurrencies(enumQueryCurrentCurrencies.ALL);
	}

	private void addCurrency(Currency currency, 
							 boolean startTransaction, 
							 boolean endTransaction){
		if (startTransaction){
			this.db.beginTransaction();
		}
		
		ContentValues cv = new ContentValues();
		cv.put(CurrencyStorageHelper.COL_CURRENCY_CODE, currency.getCode());
		cv.put(CurrencyStorageHelper.COL_CURRENCY_VALUE, currency.getValue());
		cv.put(CurrencyStorageHelper.COL_LAST_DOWNLOADED, new Date().getTime());
		cv.put(CurrencyStorageHelper.COL_LAST_UPDATE, new Date().getTime());
		
		db.insert(CurrencyStorageHelper.TABLE_CURRENCIES, null, cv);
		
		if (currency.isTarget()){
			cv.put(CurrencyStorageHelper.COL_IS_TARGET_CURRENCY, 1);
		}

		db.insert(CurrencyStorageHelper.TABLE_DEFAULT_CURRENCIES, null, cv);

		if (endTransaction){
			db.endTransaction();
		}
	}
	
	
	/**
	 * Add new currencies on the main historical table and update
	 * the cache table which will be used to show the currencies to
	 * the user.
	 * @param result
	 * @return Cursor pointing to the default currencies shown on the screen
	 */
	public Cursor addDownlodadedCurrencies(Currency[] result) {

		//Add the currencies to both the historical table and the
		//table containing the currently shown records.
		
		//First of all purge the default table...
		db.delete(CurrencyStorageHelper.TABLE_DEFAULT_CURRENCIES, null, null);
		
		//Now start adding records...
		for (int i = 0; i < result.length; i++){
			Currency curr = result[i];
			
			addCurrency(curr, (i == 0), (i == result.length - 1));
		}
		
		return this.getConvertedCurrencies();
	}
}
