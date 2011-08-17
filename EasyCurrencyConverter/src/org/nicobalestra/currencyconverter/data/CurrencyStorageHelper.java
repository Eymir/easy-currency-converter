package org.nicobalestra.currencyconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Provide utility methods for the creation of the currencies database.
 * @author DBalestra
 *
 */
public class CurrencyStorageHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	
	public static final String DATABASE_NAME = "easycurrencyconverter";
	public static final String COL_ID = "_id";
	public static final String COL_CURRENCY_CODE = "currency_code";
	public static final String COL_CURRENCY_FULL_NAME = "currency_full_name";
	public static final String COL_CURRENCY_VALUE = "currency_value";
	public static final String COL_LAST_UPDATE = "last_update";
	public static final String COL_LAST_DOWNLOADED = "last_downloaded";
	

	
	private static final String DATABASE_CREATE = 
				"CREATE TABLE currencies(" + COL_ID + " integer primary key autoincrement " +
								       ", " + COL_CURRENCY_CODE + " text not null" +
								       ", " + COL_CURRENCY_FULL_NAME + " text not null" +
								       ", " + COL_CURRENCY_VALUE + " real not null" +
								       ", " + COL_LAST_UPDATE + " text not null" +
								       ", " + COL_LAST_DOWNLOADED + " text not null)";
	
	private static final String DATABASE_DROP = "DROP TABLE IF EXISTS currencies";
	
	public CurrencyStorageHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(DATABASE_DROP);
		
		onCreate(database);
	}

}
