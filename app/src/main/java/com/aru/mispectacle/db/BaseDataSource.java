package com.aru.mispectacle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aru.mispectacle.model.Spectacle;
import com.aru.mispectacle.sqlite.SpectacleDBOpenHelper;


public class BaseDataSource {

	public static final String LOGTAG="MiSpectacleDB";

	SQLiteOpenHelper sqLiteOpenHelper;
	SQLiteDatabase database;
	
	public BaseDataSource(Context context) {
		sqLiteOpenHelper = new SpectacleDBOpenHelper(context);
	}
	
	public void open() {
		Log.i(LOGTAG, "Database opened");
		database = sqLiteOpenHelper.getWritableDatabase();
	}

	public void close() {
		Log.i(LOGTAG, "Database closed");		
		sqLiteOpenHelper.close();
	}
	

	
}
