package org.nicobalestra.currencyconverter;

import java.util.HashMap;
import java.util.Map;

import org.nicobalestra.currencyconverter.data.CurrencyStorageHelper;

import android.database.Cursor;

public class Currency {

	
	private static Map<String, Integer> imgLookup = new HashMap<String, Integer>();


	
	static{
		imgLookup.put("USD", R.drawable.united_states_flag);
		imgLookup.put("GBP", R.drawable.united_kingdom_flag);
		imgLookup.put("DKK", R.drawable.denmark32);
		imgLookup.put("JPY", R.drawable.japan_flag);
		imgLookup.put("BGN", R.drawable.bulgaria_flag);
		imgLookup.put("CZK", R.drawable.czech_republic_flag);
		imgLookup.put("HUF", R.drawable.hungary_flag);
		imgLookup.put("LTL", R.drawable.lithuania_flag);
		imgLookup.put("LVL", R.drawable.latvia_flag);
		imgLookup.put("PLN", R.drawable.poland_flag);
		imgLookup.put("RON", R.drawable.romania_flag);
		imgLookup.put("SEK", R.drawable.sweden_flag);
		imgLookup.put("CHF", R.drawable.switzerland_flag);
		imgLookup.put("NOK", R.drawable.norway_flag);
		imgLookup.put("HRK", R.drawable.croatian_flag);
		imgLookup.put("RUB", R.drawable.russia_flag);
		imgLookup.put("TRY", R.drawable.turkey_flag);
		imgLookup.put("AUD", R.drawable.australia_flag);
		imgLookup.put("BRL", R.drawable.brazil_flag);
		imgLookup.put("CAD", R.drawable.canada_flag);
		imgLookup.put("CNY", R.drawable.china_flag);
		imgLookup.put("HKD", R.drawable.hong_kong_flag);
		imgLookup.put("IDR", R.drawable.indonesia_flag);
		imgLookup.put("ILS", R.drawable.israel_flag);
		imgLookup.put("INR", R.drawable.india_flag);
		imgLookup.put("KRW", R.drawable.korea_flag);
		imgLookup.put("MXN", R.drawable.mexico_flag);
		imgLookup.put("MYR", R.drawable.malaysia32);
		imgLookup.put("NZD", R.drawable.new_zealand32);
		imgLookup.put("PHP", R.drawable.philippines_flag);
		imgLookup.put("MYR", R.drawable.malaysia32);
		imgLookup.put("SGD", R.drawable.singapore_flag);
		imgLookup.put("THB", R.drawable.thailand_flag);
		imgLookup.put("ZAR", R.drawable.south_africa_flag);
		imgLookup.put("EUR", R.drawable.south_africa_flag);
		
	}
	
	private String code;
	private String fullName;
	
	private long lastUpdateDate;
	private long lastDownloadDate;
	private double value;
	
	private int imgResourceID;
	
	private boolean isTarget;
	
	public Currency(){
		
	}
	
	public Currency(String code, double value){
		this.code = code;
		this.value = value;
		this.imgResourceID = imgLookup.get(this.code);
	}

	public Currency(String code, String fullName, double value){
		this(code, value);
		this.fullName = fullName;
	}

	public Currency(Cursor cursor){
		this.code = cursor.getString(cursor.getColumnIndex(CurrencyStorageHelper.COL_CURRENCY_CODE));
		this.fullName = cursor.getString(cursor.getColumnIndex(CurrencyStorageHelper.COL_DEFINITION_FULL_NAME));
		
		this.value = cursor.getDouble(cursor.getColumnIndex(CurrencyStorageHelper.COL_CURRENCY_VALUE));
		this.lastUpdateDate = cursor.getLong(cursor.getColumnIndex(CurrencyStorageHelper.COL_LAST_UPDATE));
		this.lastDownloadDate = cursor.getLong(cursor.getColumnIndex(CurrencyStorageHelper.COL_LAST_DOWNLOADED));
	}
	
	/**
	 * @return the name
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @param name the name to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the imgResourceID
	 */
	public int getImgResourceID() {
		return this.imgResourceID;
	}

	
	public static Map getCurrenciesDefinition(){
		return imgLookup; 
	}

	public String getFullName() {
		return this.fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isTarget() {
		return this.isTarget;
	}
	
	public void setTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}
}
