package com.xtech.sunshine_tutorial;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class WeatherContract {
    // Example of Content URI : content://com.example.android.sunshine.app/givemeroot/
    public static final String CONTENT_AUTHORITY = "com.xtech.sunshine_tutorial";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (database tables)
    public static final String PATH_WEATHER = "weather";
    public static final String PATH_LOCATION = "location";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COL_CITY_NAME = "city_name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        // Cursor returning multiple items
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        // Cursor returning a single item
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COL_MIN_TEMP = "min";
        public static final String COL_MAX_TEMP = "max";
        public static final String COL_MORN_TEMP = "morn";
        public static final String COL_EVE_TEMP = "eve";
        public static final String COL_NIGHT_TEMP = "night";
        public static final String COL_DAY_TEMP = "day";
        public static final String COL_HUMIDITY = "humidity";
        public static final String COL_LOC_KEY = "location";
        public static final String COL_DATE = "date";
        public static final String COL_DAY = "day_number";
        public static final String COL_DESC = "desc";
        public static final String COL_ICON = "icon";
        public static final String COL_WEATHER_ID = "weather_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWeatherLocation(String location) {
            return CONTENT_URI.buildUpon().appendPath(location).build();
        }

        public static Uri buildWeatherLocationWithStartDate(
                String locationSetting, String startDate) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendQueryParameter(COL_DATE, startDate).build();
        }

        public static Uri buildWeatherLocationWithDate(String locationSetting, String date) {
            return CONTENT_URI.buildUpon().appendPath(locationSetting)
                    .appendPath(date).build();
        }

        public static String getLocationFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getDateFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COL_DATE);
            if (null != dateString && dateString.length() > 0)
                return dateString;
            else
                return null;
        }
    }
}
