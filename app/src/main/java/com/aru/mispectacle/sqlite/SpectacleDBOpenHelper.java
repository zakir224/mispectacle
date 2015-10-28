package com.aru.mispectacle.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aru.mispectacle.model.Spectacle;

/**
 * Created by Md Zakir Hossen on 10/26/2015.
 */
public class SpectacleDBOpenHelper extends SQLiteOpenHelper {


    private static final String LOGTAG = "MiSpectacle";
    private static final String DATABASE_NAME = "spectacles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE_SPECTACLE =
            "CREATE TABLE " + Spectacle.TABLE_NAME + " (" +
                    Spectacle.COLUMN_SPECTACLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Spectacle.COLUMN_SPECTACLE_BRAND + " TEXT, " +
                    Spectacle.COLUMN_SPECTACLE_PRICE + " NUMERIC, " +
                    Spectacle.COLUMN_SPECTACLE_CATEGORY_ID + " INTEGER," +
                    Spectacle.COLUMN_SPECTACLE_LOCATION_URI + " TEXT" +
                    ")";



    public SpectacleDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SPECTACLE);
        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_SPECTACLE);
        onCreate(db);
    }
}
