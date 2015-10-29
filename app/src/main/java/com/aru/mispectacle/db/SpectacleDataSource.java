package com.aru.mispectacle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aru.mispectacle.model.Spectacle;
import com.aru.mispectacle.sqlite.SpectacleDBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md Zakir Hossen on 10/22/2015.
 */
public class SpectacleDataSource extends BaseDataSource {

    private static final String[] allColumns = {
            SpectacleDBOpenHelper.KEY_ID,
            Spectacle.COLUMN_SPECTACLE_BRAND,
            Spectacle.COLUMN_SPECTACLE_PRICE,
            Spectacle.COLUMN_SPECTACLE_CATEGORY_ID,
            Spectacle.COLUMN_SPECTACLE_GENDER_ID,
            Spectacle.COLUMN_SPECTACLE_LOCATION_URI};

    public SpectacleDataSource(Context context) {
        super(context);
    }

    public Spectacle create(Spectacle spectacle) {

        ContentValues values = new ContentValues();
        values.put(Spectacle.COLUMN_SPECTACLE_BRAND, spectacle.getSpectacleBrand());
        values.put(Spectacle.COLUMN_SPECTACLE_PRICE, spectacle.getSpectaclePrice());
        values.put(Spectacle.COLUMN_SPECTACLE_CATEGORY_ID, spectacle.getSpectacleCategoryId());
        values.put(Spectacle.COLUMN_SPECTACLE_GENDER_ID, spectacle.getSpectacleGenderId());
        values.put(Spectacle.COLUMN_SPECTACLE_LOCATION_URI, spectacle.getSpectacleLocationUri());

        long insertid = database.insert(Spectacle.TABLE_NAME, null, values);
        spectacle.setSpectacleId(insertid);
        return spectacle;

    }

    public List<Spectacle> getAllSpectacles() {

        Cursor cursor = database.query(SpectacleDBOpenHelper.TABLE_SPECTACLE, allColumns,
                null, null, null, null, null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        List<Spectacle> tours = cursorToList(cursor);
        return tours;
    }

    private List<Spectacle> cursorToList(Cursor cursor) {
        List<Spectacle> spectacles = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Spectacle spectacle = new Spectacle();
                spectacle.setSpectacleId(cursor.getLong(cursor.getColumnIndex(SpectacleDBOpenHelper.KEY_ID)));
                spectacle.setSpectacleBrand(cursor.getString(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_BRAND)));
                spectacle.setSpectacleCategoryId(cursor.getLong(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_CATEGORY_ID)));
                spectacle.setSpectacleGenderId(cursor.getLong(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_GENDER_ID)));
                spectacle.setSpectacleLocationUri(cursor.getString(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_LOCATION_URI)));
                spectacles.add(spectacle);
            }
        }
        return spectacles;
    }

    public Spectacle getSpectacle(long id) {

        Spectacle spectacle = new Spectacle();
        Cursor cursor = database.query(SpectacleDBOpenHelper.TABLE_SPECTACLE, allColumns, SpectacleDBOpenHelper.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        do {
            spectacle.setSpectacleId(cursor.getLong(cursor.getColumnIndex(SpectacleDBOpenHelper.KEY_ID)));
            spectacle.setSpectacleBrand(cursor.getString(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_BRAND)));
            spectacle.setSpectacleCategoryId(cursor.getLong(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_CATEGORY_ID)));
            spectacle.setSpectacleGenderId(cursor.getLong(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_GENDER_ID)));
            spectacle.setSpectacleLocationUri(cursor.getString(cursor.getColumnIndex(Spectacle.COLUMN_SPECTACLE_LOCATION_URI)));
        } while (cursor.moveToNext());


        return spectacle;
    }
}
