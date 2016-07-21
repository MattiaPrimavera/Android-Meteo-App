package com.xtech.sunshine_tutorial;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class WeatherProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder weatherByLocationQueryBuilder;
    private WeatherDbHelper dbHelper;
    private SQLiteDatabase db;

    static final int WEATHER = 100;
    static final int WEATHER_WITH_LOCATION = 101;
    static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int LOCATION = 300;

    public WeatherProvider(){
        dbHelper = new WeatherDbHelper(getContext());
    }


    static{
        weatherByLocationQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        weatherByLocationQueryBuilder.setTables(
                WeatherContract.WeatherEntry.TABLE_NAME + " INNER JOIN " +
                        WeatherContract.LocationEntry.TABLE_NAME +
                        " ON " + WeatherContract.WeatherEntry.TABLE_NAME +
                        "." + WeatherContract.WeatherEntry.COL_LOC_KEY +
                        " = " + WeatherContract.LocationEntry.TABLE_NAME +
                        "." + WeatherContract.LocationEntry._ID);
    }

    private static final String locationSelection =
            WeatherContract.LocationEntry.TABLE_NAME+
                    "." + WeatherContract.LocationEntry.COL_CITY_NAME + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String locationWithStartDateSelection =
            WeatherContract.LocationEntry.TABLE_NAME+
                    "." + WeatherContract.LocationEntry.COL_CITY_NAME + " = ? AND " +
                    WeatherContract.WeatherEntry.COL_DATE + " >= ? ";

    private static final String locationAndDaySelection =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry.COL_CITY_NAME + " = ? AND " +
                    WeatherContract.WeatherEntry.COL_DATE + " = ? ";

    private Cursor getWeatherByLocation(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.WeatherEntry.getLocationFromUri(uri);
        String startDate = WeatherContract.WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        if (startDate == null) {
            selection = locationSelection;
            selectionArgs = new String[]{locationSetting};
        } else {
            selectionArgs = new String[]{locationSetting, startDate};
            selection = locationWithStartDateSelection;
        }

        return weatherByLocationQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherByLocationAndDate(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.WeatherEntry.getLocationFromUri(uri);
        String date = WeatherContract.WeatherEntry.getDateFromUri(uri);

        return weatherByLocationQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                locationAndDaySelection,
                new String[]{locationSetting, date},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, WeatherContract.PATH_WEATHER, WEATHER);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);
        matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        Log.d("content provider: ", "function getType called");
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = uriMatcher.match(uri);

        switch (match) {
            case WEATHER_WITH_LOCATION_AND_DATE:
                Log.d("content provider: ", WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE);
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                Log.d("content provider: ", WeatherContract.WeatherEntry.CONTENT_TYPE);
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case WEATHER:
                Log.d("content provider: ", WeatherContract.WeatherEntry.CONTENT_TYPE);
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
            case LOCATION:
                Log.d("content provider: ", WeatherContract.WeatherEntry.CONTENT_TYPE);
                return WeatherContract.LocationEntry.CONTENT_TYPE;
            default:
                Log.d("content provider: ", "exception");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            // "weather/*/*"
            case WEATHER_WITH_LOCATION_AND_DATE:
            {
                retCursor = getWeatherByLocationAndDate(uri, projection, sortOrder);
                break;
            }
            // "weather/*"
            case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocation(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case WEATHER: {
                retCursor = this.dbHelper.getReadableDatabase().query(WeatherContract.WeatherEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
                break;
            }
            // "location"
            case LOCATION: {
                retCursor = this.dbHelper.getReadableDatabase().query(WeatherContract.LocationEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Registers an observer for the content URI to notify the UI of changes
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        this.db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WEATHER: {
                long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = WeatherContract.WeatherEntry.buildWeatherUri(_id);
                    Log.d("content-resolver returnUri weather: ", returnUri.toString());
                }
                else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case LOCATION: {
                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
                if ( _id > 0 ) {
                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
                    Log.d("content-resolver returnURi location:", returnUri.toString());
                }
                else{
                    Log.d("content-resolver ", "location");
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notifying changes on the passed-in uri, not the return uri
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        this.db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int deleted;

        // If you delete all rows, it will return the number of deleted rows
        if(selection == null) selection = "1";

        switch (match) {
            case WEATHER: {
                deleted = db.delete(WeatherContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case LOCATION: {
                deleted = db.delete(WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(deleted != 0) {
            // Notifying changes on the passed-in uri, not the return uri
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        this.db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int updated;

        // If you delete all rows, it will return the number of deleted rows
        if(selection == null) selection = "1";

        switch (match) {
            case WEATHER: {
                updated = db.update(WeatherContract.WeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case LOCATION: {
                updated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(updated > 0) {
            // Notifying changes on the passed-in uri, not the return uri
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        db.close();
        super.shutdown();
    }
}