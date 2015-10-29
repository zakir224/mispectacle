package com.aru.mispectacle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToursDBOpenHelper extends SQLiteOpenHelper {

	private static final String LOGTAG = "EXPLORECA";

	private static final String DATABASE_NAME = "tours.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_TOURS = "tours";
	public static final String COLUMN_ID = "tourId";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_IMAGE = "image";
	
	private static final String TABLE_CREATE = 
			"CREATE TABLE " + TABLE_TOURS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_TITLE + " TEXT, " +
			COLUMN_DESC + " TEXT, " +
			COLUMN_IMAGE + " TEXT, " +
			COLUMN_PRICE + " NUMERIC " +
			")";
			
	
	public ToursDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		Log.i(LOGTAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
		onCreate(db);
	}



	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME , null, DATABASE_VERSION);
	}




	private static final String CREATE_TABLE_TYPE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_TYPE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + Type.TYPE_NAME
			+ " TEXT)";

	private static final String CREATE_TABLE_GROUP = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_GROUP + "(" + KEY_ID + " INTEGER PRIMARY KEY," + Group.GROUP_NAME
			+ " TEXT)";

	private static final String CREATE_TABLE_CONTACT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_CONTACT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + Contact.CONTACT_NAME
			+ " TEXT," + Contact.CONTACT_ADDRESS+ " TEXT," +Contact.CONTACT_EMAIL+" TEXT," +Contact.HOME_PHN+" TEXT,"+Contact.OFFICE_PHN+
			"TEXT,"+Contact.OTHERS+" TEXT,"+Contact.GROUP_ID+" INTEGER,"+Contact.TYPE_ID+" INTEGER,"+ Contact.CONTACT_PHOTO+ " TEXT)";


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TYPE);
		Log.d(LOG, CREATE_TABLE_TYPE);
//        db.execSQL(CREATE_TABLE_GROUP);
//        Log.d(LOG,CREATE_TABLE_GROUP);
		db.execSQL(CREATE_TABLE_CONTACT);
		Log.d(LOG,CREATE_TABLE_CONTACT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
		onCreate(db);
	}

	//-------------create contact-----------------//

	public void createContact(Contact con,SQLiteDatabase db) {

		try {
			ContentValues values = new ContentValues();
			values.put(Contact.CONTACT_NAME, con.getContactName());
			values.put(Contact.HOME_PHN, UiUtils.getString(con.getHomePhone()));
			values.put(Contact.CONTACT_EMAIL, con.getEmail());
			values.put(Contact.CONTACT_PHOTO, UiUtils.getString(con.getPhoto()));
			long value = db.insert(TABLE_CONTACT, null, values);
			Log.d("value",String.valueOf(value));
		} catch (Exception e) {
			Log.d("zakir",e.toString());
		}
	}

	//-------------Delete contact-------------//
	public void deleteContact(Contact con) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACT, KEY_ID + " = ?",
				new String[] { String.valueOf(con.getContactId()) });
		db.close();
	}
	//-------------Update contact-------------//

	public int updateContact(Contact con) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Contact.CONTACT_NAME, con.getContactName());
		values.put(Contact.CONTACT_EMAIL, con.getEmail());

		return db.update(TABLE_CONTACT, values, KEY_ID + " = ?",
				new String[] {  String.valueOf(con.getContactId()) });
	}

	//-----------------get Contact ---------------//

	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Contact contact = new Contact();
		Cursor cursor = db.query(TABLE_CONTACT, new String[] { KEY_ID,
						Contact.CONTACT_NAME, Contact.CONTACT_EMAIL }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		db.close();
		if (cursor != null)
			cursor.moveToFirst();
		do{
			contact.setContactName(cursor.getString(cursor.getColumnIndex(Contact.CONTACT_NAME)));
			contact.setEmail(cursor.getString(cursor.getColumnIndex(Contact.CONTACT_EMAIL)));
			contact.setHomePhone(cursor.getString(cursor.getColumnIndex(Contact.HOME_PHN)));
			contact.setOfficePhnNo(cursor.getString(cursor.getColumnIndex(Contact.OFFICE_PHN)));
			contact.setAddress(cursor.getString(cursor.getColumnIndex(Contact.CONTACT_ADDRESS)));

		}while (cursor.moveToNext());
		// return contact
		return contact;
	}

	public List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACT;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setContactId(c.getInt((c.getColumnIndex(KEY_ID))));
				contact.setContactName((c.getString(c.getColumnIndex(Contact.CONTACT_NAME))));
				contact.setHomePhone((c.getString(c.getColumnIndex(Contact.HOME_PHN))));
				contacts.add(contact);
			} while (c.moveToNext());
		}

		return contacts;
	}
}
