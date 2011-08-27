package org.nicobalestra.currencyconverter.data;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Provide utility methods for the creation of the currencies database.
 * @author DBalestra
 *
 */
public class CurrencyStorageHelper extends SQLiteOpenHelper {
	private static final String TAG = "CurrencyStorageHelper";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_PATH = "/data/data/org.nicobalestra.currencyconverter/databases/";
	
	public static final String DATABASE_NAME = "easycurrencyconverter.db";
	public static final String COL_ID = "_id";
	
	public static final String COL_CURRENCY_CODE = "currency_code";
	public static final String COL_CURRENCY_VALUE = "currency_value";
	public static final String COL_LAST_UPDATE = "last_update";
	public static final String COL_LAST_DOWNLOADED = "last_downloaded";
	public static final String COL_IS_TARGET_CURRENCY = "is_target_currency";
	public static final String COL_CURRENCY_POSITION = "position";
	
	public static final String TABLE_DEFAULT_CURRENCIES = "defaultcurrencies";
	public static final String TABLE_CURRENCIES = "currencyhistory";
	public static final String TABLE_CURRENCY_DEFINITION = "currencydefinition";

	public static final String COL_DEFINITION_CODE = "code";
	public static final String COL_DEFINITION_FULL_NAME = "full_name";

	

	
	private static final String DATABASE_CREATE_1 = 
				"CREATE TABLE " + TABLE_CURRENCIES + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
								       ", " + COL_CURRENCY_CODE + " TEXT NOT NULL" +
								       ", " + COL_CURRENCY_VALUE + " REAL NOT NULL" +
								       ", " + COL_LAST_UPDATE + " REAL NOT NULL" +
								       ", " + COL_LAST_DOWNLOADED + " REAL NOT NULL); ";
	private static final String DATABASE_CREATE_2 =	
				"CREATE TABLE " + TABLE_DEFAULT_CURRENCIES + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
								       ", " + COL_CURRENCY_CODE + " TEXT NOT NULL" +
								       ", " + COL_CURRENCY_VALUE + " REAL NOT NULL" +
								       ", " + COL_CURRENCY_POSITION + " INTEGER NOT NULL" +
								       ", " + COL_LAST_UPDATE + " REAL NOT NULL" +
								       ", " + COL_LAST_DOWNLOADED + " REAL NOT NULL" + 
								       ", " + COL_IS_TARGET_CURRENCY + " NUMERIC ); ";
	private static final String DATABASE_CREATE_3 =
				"CREATE TABLE " + TABLE_CURRENCY_DEFINITION + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
								       ", " + COL_DEFINITION_CODE + " TEXT NOT NULL" +
								       ", " + COL_DEFINITION_FULL_NAME + " TEXT NOT NULL); ";
	private static final String DATABASE_CREATE_4 = "COMMIT;";
 			
	
	
	private static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_CURRENCIES + "; " +
												"DROP TABLE IF EXISTS " + TABLE_DEFAULT_CURRENCIES + ";" + 
												"DROP TABLE IF EXISTS " + TABLE_CURRENCY_DEFINITION + ";";
	
	public CurrencyStorageHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.d(TAG, "Opening the database... " + db.getPath() + " version " + db.getVersion());
		db.setLockingEnabled(true);
	} 
	
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		//database = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME, null);
		try{
		if (database.isDbLockedByCurrentThread()){
			Log.d(TAG, "Database locked by current thread...");
		}
		
		
		if (database.isDbLockedByOtherThreads()){
			Log.e(TAG, "Database locked by OTHER thread...");
		}
		
		if (database.isOpen()){
			Log.d(TAG, "OK.. Database open");
		}
		
		if (database.isReadOnly()){
			Log.e(TAG, "The database is read only");
		}
		
		if (database.inTransaction()){
			Log.e(TAG, "Why id the databse in transaction???");
		}
		
		Log.d(TAG, "Call to onCreate");

		Log.d(TAG, "Creating table..." + DATABASE_CREATE_1);
		
		database.execSQL(DATABASE_CREATE_1);
		
		Log.d(TAG, "Creating table..." + DATABASE_CREATE_2);
		database.execSQL(DATABASE_CREATE_2);
		
		Log.d(TAG, "Creating table..." + DATABASE_CREATE_3);		
		database.execSQL(DATABASE_CREATE_3);
		
		database.execSQL(DATABASE_CREATE_4);
		insertDefinitions(database);
		insertInitialCurrencies(database);
		}
		catch(Exception e){
			Log.e(TAG, "Erroreeeeeeeeeee" , e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(DATABASE_DROP);
		
		onCreate(database);
	}

	/**
	 * Create the default values to be shown to the user main activity when the application
	 * is ran for the first time and no currencies have been downloaded yet.
	 * @param database
	 */
	private void insertInitialCurrencies(SQLiteDatabase database){

		int i = 1;
		//TODO The fact that USD is the target currency is arbitrary. Maybe we should read user's
		//locale to get her currency
		insertDefaultCurrency(database, "USD", "U.S. Dollar", 1, i++, true, (new Date()).getTime(), (new Date()).getTime(), true, false);
		insertDefaultCurrency(database, "EUR", "Euro", 1, i++, false, (new Date()).getTime(), (new Date()).getTime(), false, false);
		insertDefaultCurrency(database, "GBP", "British Pound", 1, i++, false, (new Date()).getTime(), (new Date()).getTime(), false, false);
		insertDefaultCurrency(database, "JPY", "Japanese Yen", 1, i++, false, (new Date()).getTime(), (new Date()).getTime(), false, false);
		insertDefaultCurrency(database, "CNY", "Chinese Yuan", 1, i++, false, (new Date()).getTime(), (new Date()).getTime(), false, false);
		insertDefaultCurrency(database, "CHF", "Swiss Franc", 1, i++, false, (new Date()).getTime(), (new Date()).getTime(), false, true);
		

	}
	
	/**
	 * Insert a new currency value which will be shown on the user main activity.
	 * 
	 * @param database
	 * @param currencyCode
	 * @param currencyFullName
	 * @param value
	 * @param position
	 * @param isTargetCurrency
	 * @param lastModifiedDate
	 * @param lastDownloadedDate
	 * @param startTransaction
	 * @param endTransaction
	 */
	private void insertDefaultCurrency(SQLiteDatabase database, 
								String currencyCode, 
								String currencyFullName, 
								double value, 
								int position,
								boolean isTargetCurrency,
								long lastModifiedDate,
								long lastDownloadedDate,
								boolean startTransaction,
								boolean endTransaction){
		ContentValues vals = new ContentValues();
		vals.put(COL_CURRENCY_CODE, currencyCode);
		vals.put(COL_CURRENCY_VALUE, value);
		vals.put(COL_LAST_DOWNLOADED, lastDownloadedDate);
		vals.put(COL_LAST_UPDATE, lastModifiedDate);
		vals.put(COL_IS_TARGET_CURRENCY, (isTargetCurrency? 1 : 0));
		vals.put(COL_CURRENCY_POSITION, position);
		
		if (startTransaction){
			database.beginTransaction();
		}
		Log.d(TAG, "Adding default currency..." + currencyCode);
		database.insertOrThrow(TABLE_DEFAULT_CURRENCIES, null, vals);
	
		if (endTransaction){
			database.endTransaction();
		}
	}


	private void insertDefinitions(SQLiteDatabase database){
		database.beginTransaction();
		try{
			insertSingleDefinition(database, "AFA", "Afghanistan Afghani", true, false);
			insertSingleDefinition(database, "ALL", "Albanian Lek");
			insertSingleDefinition(database, "DZD", "Algerian Dinar");
			insertSingleDefinition(database, "ARS", "Argentine Peso");
			insertSingleDefinition(database, "AWG", "Aruba Florin");
			insertSingleDefinition(database, "AUD", "Australian Dollar");
			insertSingleDefinition(database, "BSD", "Bahamian Dollar");
			insertSingleDefinition(database, "BHD", "Bahraini Dinar");
			insertSingleDefinition(database, "BDT", "Bangladesh Taka");
			insertSingleDefinition(database, "BBD", "Barbados Dollar");
			insertSingleDefinition(database, "BZD", "Belize Dollar");
			insertSingleDefinition(database, "BMD", "Bermuda Dollar");
			insertSingleDefinition(database, "BTN", "Bhutan Ngultrum");
			insertSingleDefinition(database, "BOB", "Bolivian Boliviano");
			insertSingleDefinition(database, "BWP", "Botswana Pula");
			insertSingleDefinition(database, "BRL", "Brazilian Real");
			insertSingleDefinition(database, "GBP", "British Pound");
			insertSingleDefinition(database, "BND", "Brunei Dollar");
			insertSingleDefinition(database, "BIF", "Burundi Franc");
			insertSingleDefinition(database, "XOF", "CFA Franc (BCEAO)");
			insertSingleDefinition(database, "XAF", "CFA Franc (BEAC)");
			insertSingleDefinition(database, "KHR", "Cambodia Riel");
			insertSingleDefinition(database, "CAD", "Canadian Dollar");
			insertSingleDefinition(database, "CVE", "Cape Verde Escudo");
			insertSingleDefinition(database, "KYD", "Cayman Islands Dollar");
			insertSingleDefinition(database, "CLP", "Chilean Peso");
			insertSingleDefinition(database, "CNY", "Chinese Yuan");
			insertSingleDefinition(database, "COP", "Colombian Peso");
			insertSingleDefinition(database, "KMF", "Comoros Franc");
			insertSingleDefinition(database, "CRC", "Costa Rica Colon");
			insertSingleDefinition(database, "HRK", "Croatian Kuna");
			insertSingleDefinition(database, "CUP", "Cuban Peso");
			insertSingleDefinition(database, "CYP", "Cyprus Pound");
			insertSingleDefinition(database, "CZK", "Czech Koruna");
			insertSingleDefinition(database, "DKK", "Danish Krone");
			insertSingleDefinition(database, "DJF", "Dijibouti Franc");
			insertSingleDefinition(database, "DOP", "Dominican Peso");
			insertSingleDefinition(database, "XCD", "East Caribbean Dollar");
			insertSingleDefinition(database, "EGP", "Egyptian Pound");
			insertSingleDefinition(database, "SVC", "El Salvador Colon");
			insertSingleDefinition(database, "EEK", "Estonian Kroon");
			insertSingleDefinition(database, "ETB", "Ethiopian Birr");
			insertSingleDefinition(database, "EUR", "Euro");
			insertSingleDefinition(database, "FKP", "Falkland Islands Pound");
			insertSingleDefinition(database, "GMD", "Gambian Dalasi");
			insertSingleDefinition(database, "GHC", "Ghanian Cedi");
			insertSingleDefinition(database, "GIP", "Gibraltar Pound");
			insertSingleDefinition(database, "XAU", "Gold Ounces");
			insertSingleDefinition(database, "GTQ", "Guatemala Quetzal");
			insertSingleDefinition(database, "GNF", "Guinea Franc");
			insertSingleDefinition(database, "GYD", "Guyana Dollar");
			insertSingleDefinition(database, "HTG", "Haiti Gourde");
			insertSingleDefinition(database, "HNL", "Honduras Lempira");
			insertSingleDefinition(database, "HKD", "Hong Kong Dollar");
			insertSingleDefinition(database, "HUF", "Hungarian Forint");
			insertSingleDefinition(database, "ISK", "Iceland Krona");
			insertSingleDefinition(database, "INR", "Indian Rupee");
			insertSingleDefinition(database, "IDR", "Indonesian Rupiah");
			insertSingleDefinition(database, "IQD", "Iraqi Dinar");
			insertSingleDefinition(database, "ILS", "Israeli Shekel");
			insertSingleDefinition(database, "JMD", "Jamaican Dollar");
			insertSingleDefinition(database, "JPY", "Japanese Yen");
			insertSingleDefinition(database, "JOD", "Jordanian Dinar");
			insertSingleDefinition(database, "KZT", "Kazakhstan Tenge");
			insertSingleDefinition(database, "KES", "Kenyan Shilling");
			insertSingleDefinition(database, "KRW", "Korean Won");
			insertSingleDefinition(database, "KWD", "Kuwaiti Dinar");
			insertSingleDefinition(database, "LAK", "Lao Kip");
			insertSingleDefinition(database, "LVL", "Latvian Lat");
			insertSingleDefinition(database, "LBP", "Lebanese Pound");
			insertSingleDefinition(database, "LSL", "Lesotho Loti");
			insertSingleDefinition(database, "LRD", "Liberian Dollar");
			insertSingleDefinition(database, "LYD", "Libyan Dinar");
			insertSingleDefinition(database, "LTL", "Lithuanian Lita");
			insertSingleDefinition(database, "MOP", "Macau Pataca");
			insertSingleDefinition(database, "MKD", "Macedonian Denar");
			insertSingleDefinition(database, "MGF", "Malagasy Franc");
			insertSingleDefinition(database, "MWK", "Malawi Kwacha");
			insertSingleDefinition(database, "MYR", "Malaysian Ringgit");
			insertSingleDefinition(database, "MVR", "Maldives Rufiyaa");
			insertSingleDefinition(database, "MTL", "Maltese Lira");
			insertSingleDefinition(database, "MRO", "Mauritania Ougulya");
			insertSingleDefinition(database, "MUR", "Mauritius Rupee");
			insertSingleDefinition(database, "MXN", "Mexican Peso");
			insertSingleDefinition(database, "MDL", "Moldovan Leu");
			insertSingleDefinition(database, "MNT", "Mongolian Tugrik");
			insertSingleDefinition(database, "MAD", "Moroccan Dirham");
			insertSingleDefinition(database, "MZM", "Mozambique Metical");
			insertSingleDefinition(database, "MMK", "Myanmar Kyat");
			insertSingleDefinition(database, "NAD", "Namibian Dollar");
			insertSingleDefinition(database, "NPR", "Nepalese Rupee");
			insertSingleDefinition(database, "ANG", "Neth Antilles Guilder");
			insertSingleDefinition(database, "NZD", "New Zealand Dollar");
			insertSingleDefinition(database, "NIO", "Nicaragua Cordoba");
			insertSingleDefinition(database, "NGN", "Nigerian Naira");
			insertSingleDefinition(database, "KPW", "North Korean Won");
			insertSingleDefinition(database, "NOK", "Norwegian Krone");
			insertSingleDefinition(database, "OMR", "Omani Rial");
			insertSingleDefinition(database, "XPF", "Pacific Franc");
			insertSingleDefinition(database, "PKR", "Pakistani Rupee");
			insertSingleDefinition(database, "XPD", "Palladium Ounces");
			insertSingleDefinition(database, "PAB", "Panama Balboa");
			insertSingleDefinition(database, "PGK", "Papua New Guinea Kina");
			insertSingleDefinition(database, "PYG", "Paraguayan Guarani");
			insertSingleDefinition(database, "PEN", "Peruvian Nuevo Sol");
			insertSingleDefinition(database, "PHP", "Philippine Peso");
			insertSingleDefinition(database, "XPT", "Platinum Ounces");
			insertSingleDefinition(database, "PLN", "Polish Zloty");
			insertSingleDefinition(database, "QAR", "Qatar Rial");
			insertSingleDefinition(database, "ROL", "Romanian Leu");
			insertSingleDefinition(database, "RUB", "Russian Rouble");
			insertSingleDefinition(database, "WST", "Samoa Tala");
			insertSingleDefinition(database, "STD", "Sao Tome Dobra");
			insertSingleDefinition(database, "SAR", "Saudi Arabian Riyal");
			insertSingleDefinition(database, "SCR", "Seychelles Rupee");
			insertSingleDefinition(database, "SLL", "Sierra Leone Leone");
			insertSingleDefinition(database, "XAG", "Silver Ounces");
			insertSingleDefinition(database, "SGD", "Singapore Dollar");
			insertSingleDefinition(database, "SKK", "Slovak Koruna");
			insertSingleDefinition(database, "SIT", "Slovenian Tolar");
			insertSingleDefinition(database, "SBD", "Solomon Islands Dollar");
			insertSingleDefinition(database, "SOS", "Somali Shilling");
			insertSingleDefinition(database, "ZAR", "South African Rand");
			insertSingleDefinition(database, "LKR", "Sri Lanka Rupee");
			insertSingleDefinition(database, "SHP", "St Helena Pound");
			insertSingleDefinition(database, "SDD", "Sudanese Dinar");
			insertSingleDefinition(database, "SRG", "Surinam Guilder");
			insertSingleDefinition(database, "SZL", "Swaziland Lilageni");
			insertSingleDefinition(database, "SEK", "Swedish Krona");
			insertSingleDefinition(database, "TRY", "Turkey Lira");
			insertSingleDefinition(database, "CHF", "Swiss Franc");
			insertSingleDefinition(database, "SYP", "Syrian Pound");
			insertSingleDefinition(database, "TWD", "Taiwan Dollar");
			insertSingleDefinition(database, "TZS", "Tanzanian Shilling");
			insertSingleDefinition(database, "THB", "Thai Baht");
			insertSingleDefinition(database, "TOP", "Tonga Pa'anga");
			insertSingleDefinition(database, "TTD", "Trinidad&amp;Tobago Dollar");
			insertSingleDefinition(database, "TND", "Tunisian Dinar");
			insertSingleDefinition(database, "TRL", "Turkish Lira");
			insertSingleDefinition(database, "USD", "U.S. Dollar");
			insertSingleDefinition(database, "AED", "UAE Dirham");
			insertSingleDefinition(database, "UGX", "Ugandan Shilling");
			insertSingleDefinition(database, "UAH", "Ukraine Hryvnia");
			insertSingleDefinition(database, "UYU", "Uruguayan New Peso");
			insertSingleDefinition(database, "VUV", "Vanuatu Vatu");
			insertSingleDefinition(database, "VEB", "Venezuelan Bolivar");
			insertSingleDefinition(database, "VND", "Vietnam Dong");
			insertSingleDefinition(database, "YER", "Yemen Riyal");
			insertSingleDefinition(database, "YUM", "Yugoslav Dinar");
			insertSingleDefinition(database, "ZMK", "Zambian Kwacha");
			insertSingleDefinition(database, "ZWD", "Zimbabwe Dollar", false, true);
			database.setTransactionSuccessful();
		}
		finally{
			database.endTransaction();
		}
		
	}
	
	private void insertSingleDefinition(SQLiteDatabase database, String code, String fullName, boolean beginTransaction, boolean endTransaction){
		ContentValues vals = new ContentValues();
		vals.put(COL_DEFINITION_CODE, code);
		vals.put(COL_DEFINITION_FULL_NAME, fullName);
		
		if (beginTransaction){
			database.beginTransaction();
		}
		
		database.insertOrThrow(TABLE_CURRENCY_DEFINITION, null, vals);
		
		if(endTransaction){
			database.endTransaction();
		}
		
	}
	
	private void insertSingleDefinition(SQLiteDatabase database, String code, String fullName){
		insertSingleDefinition(database, code, fullName, false, false);
	}
}
