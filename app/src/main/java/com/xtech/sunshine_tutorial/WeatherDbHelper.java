package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.xtech.sunshine_tutorial.WeatherContract.LocationEntry;
import com.xtech.sunshine_tutorial.WeatherContract.WeatherEntry;

public class WeatherDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "sunshine.db";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.createLocationTable(sqLiteDatabase);
        this.createWeatherTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void createWeatherTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                // the ID of the location entry associated with this weather data
                WeatherEntry.COL_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherEntry.COL_DATE + " INTEGER NOT NULL, " +
                WeatherEntry.COL_DESC + " TEXT NOT NULL, " +
                WeatherEntry.COL_WEATHER_ID + " INTEGER NOT NULL," +
                WeatherEntry.COL_MIN_TEMP + " REAL NOT NULL, " +
                WeatherEntry.COL_MAX_TEMP + " REAL NOT NULL, " +
                WeatherEntry.COL_HUMIDITY + " REAL NOT NULL, " +

                // Location column as foreign key to location table.
                " FOREIGN KEY (" + WeatherEntry.COL_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                " UNIQUE (" + WeatherEntry.COL_DATE + ", " +
                WeatherEntry.COL_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    public void createLocationTable(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationEntry.COL_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COL_LOCATION_SETTING + " INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }
}

