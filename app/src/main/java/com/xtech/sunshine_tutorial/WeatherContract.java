package com.xtech.sunshine_tutorial;

import android.provider.BaseColumns;

public class WeatherContract {
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COL_CITY_NAME = "city_name";
    }
    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String  COL_MIN_TEMP = "min";
        public static final String  COL_MAX_TEMP = "max";
        public static final String  COL_HUMIDITY = "humidity";
        public static final String COL_LOC_KEY = "location";
        public static final String COL_DATE = "date";
        public static final String COL_DESC = "desc";
        public static final String COL_WEATHER_ID = "weather_id";
    }
}
