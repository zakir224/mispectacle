package com.aru.mispectacle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Md Zakir Hossen on 10/07/2015.
 */
public class SpectacleDatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String SPECTACLE_DATABASE_NAME = "spectacle.db";
    public static final int SPECTACLE_DATABASE_VERSION = 1;


    public SpectacleDatabaseOpenHelper(Context context) {
        super(context, SPECTACLE_DATABASE_NAME, null, SPECTACLE_DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
