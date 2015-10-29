package com.aru.mispectacle.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aru.mispectacle.model.FaceShape;
import com.aru.mispectacle.model.Gender;
import com.aru.mispectacle.model.Spectacle;
import com.aru.mispectacle.model.SpectacleCategory;
import com.aru.mispectacle.model.SpectacleList;

/**
 * Created by Md Zakir Hossen on 10/26/2015.
 */
public class SpectacleDBOpenHelper extends SQLiteOpenHelper {


    private static final String LOGTAG = "MiSpectacle";
    private static final String DATABASE_NAME = "spectacles.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GENDER = "gender";
    public static final String TABLE_SHAPE = "shape";
    public static final String TABLE_SPECTACLE = "spectacle";
    public static final String TABLE_SPECTACLE_LIST = "spectacleList";
    public static final String TABLE_SPECTACLE_CATEGORY = "spectacle";
    public static final String KEY_ID = "id";


    private static final String TABLE_CREATE_SPECTACLE =
            "CREATE TABLE IF NOT EXIST " + TABLE_SPECTACLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Spectacle.COLUMN_SPECTACLE_BRAND + " TEXT, " +
                    Spectacle.COLUMN_SPECTACLE_PRICE + " NUMERIC, " +
                    Spectacle.COLUMN_SPECTACLE_CATEGORY_ID + " INTEGER," +
                    Spectacle.COLUMN_SPECTACLE_LOCATION_URI + " TEXT" +
                    ")";

    private static final String CREATE_TABLE_GENDER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_GENDER + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    Gender.COLUMN_GENDER_NAME + " TEXT)";

    private static final String CREATE_TABLE_SHAPE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SHAPE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    FaceShape.COLUMN_FACE_SHAPE_NAME + " TEXT)";

     private static final String CREATE_TABLE_SPECTACLE_CATEGORY =
             "CREATE TABLE IF NOT EXISTS " + TABLE_SPECTACLE_CATEGORY + " (" +
                     KEY_ID + " INTEGER PRIMARY KEY, " +
                     SpectacleCategory.COLUMN_CATEGORY_NAME + " TEXT)";

    private static final String CREATE_TABLE_SPECTACLE_LIST =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SPECTACLE_LIST + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    SpectacleList.COLUMN_CATEGORY_ID + " INTEGER," +
                    SpectacleList.COLUMN_SHAPE_ID + " INTEGER," +
                    SpectacleList.COLUMN_GENDER_ID +" INTEGER)";


    public SpectacleDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SPECTACLE);
        db.execSQL(CREATE_TABLE_GENDER);
        db.execSQL(CREATE_TABLE_SHAPE);
        db.execSQL(CREATE_TABLE_SPECTACLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SPECTACLE_LIST);
        Log.i(LOGTAG, "Tables have been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_SPECTACLE);
        onCreate(db);
    }
}
