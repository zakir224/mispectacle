package com.aru.mispectacle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aru.mispectacle.model.Spectacle;
import com.aru.mispectacle.sqlite.SpectacleDBOpenHelper;


public class SpectacleDataSource {

	public static final String LOGTAG="MiSpectacleDB";
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	public SpectacleDataSource(Context context) {
		dbhelper = new SpectacleDBOpenHelper(context);
	}
	
	public void open() {
		Log.i(LOGTAG, "Database opened");
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "Database closed");		
		dbhelper.close();
	}
	
	public Spectacle create(Spectacle spectacle) {

		ContentValues values = new ContentValues();
		values.put(Spectacle.COLUMN_SPECTACLE_BRAND, spectacle.getSpectacleBrand());
		values.put(Spectacle.COLUMN_SPECTACLE_PRICE, spectacle.getSpectaclePrice());
		values.put(Spectacle.COLUMN_SPECTACLE_CATEGORY_ID, spectacle.getSpectacleIdCategoryId());
		values.put(Spectacle.COLUMN_SPECTACLE_GENDER_ID, spectacle.getSpectacleGenderId());
		values.put(Spectacle.COLUMN_SPECTACLE_LOCATION_URI, spectacle.getSpectacleLocationUri());

		long insertid = database.insert(Spectacle.TABLE_NAME, null, values);
		spectacle.setSpectacleId(insertid);
		return spectacle;

	}
	
}
