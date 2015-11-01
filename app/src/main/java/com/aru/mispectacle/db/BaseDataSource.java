package com.aru.mispectacle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aru.mispectacle.exception.MiSpectacleLog;
import com.aru.mispectacle.model.Spectacle;
import com.aru.mispectacle.sqlite.SpectacleDBOpenHelper;


public class BaseDataSource {

    public static final String LOGTAG = "MiSpectacleDB";

    private SpectacleDBOpenHelper sqLiteOpenHelper;
    protected SQLiteDatabase database;

    public BaseDataSource(Context context) {
        sqLiteOpenHelper = new SpectacleDBOpenHelper(context);
    }

    public void open() {
        MiSpectacleLog.i("Opening Database");
        database = sqLiteOpenHelper.getWritableDatabase();
        MiSpectacleLog.i("Database opened");
    }

    public void close() {
        MiSpectacleLog.i("Database closing");
        sqLiteOpenHelper.close();
        MiSpectacleLog.i("Database closed");
    }


}
