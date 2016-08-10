package com.xtech.sunshine_tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class Utility {
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key),
                context.getString(R.string.pref_location_default));
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }

    public static ContentValues[] forecastArrayToContentValues(ArrayList<Forecast> forecast, long location_id) {
        ContentValues[] result = new ContentValues[forecast.size()];
        for (int i = 0; i < forecast.size(); i++) {
            ContentValues c = new ContentValues();
            c.put(WeatherContract.WeatherEntry.COL_LOC_KEY, location_id);
            c.put(WeatherContract.WeatherEntry.COL_DATE, forecast.get(i).getDayNumber());
            c.put(WeatherContract.WeatherEntry.COL_DESC, forecast.get(i).getMain());
            c.put(WeatherContract.WeatherEntry.COL_HUMIDITY, forecast.get(i).getUmidity());
            c.put(WeatherContract.WeatherEntry.COL_MAX_TEMP, forecast.get(i).getTempMax());
            c.put(WeatherContract.WeatherEntry.COL_MIN_TEMP, forecast.get(i).getTempMin());
            c.put(WeatherContract.WeatherEntry.COL_WEATHER_ID, "4342");
            c.put(WeatherContract.WeatherEntry.COL_ICON, forecast.get(i).getIconName());
            c.put(WeatherContract.WeatherEntry.COL_DAY, forecast.get(i).getDayNumber());

            c.put(WeatherContract.WeatherEntry.COL_DAY_TEMP, forecast.get(i).getTempDay());
            c.put(WeatherContract.WeatherEntry.COL_MORN_TEMP, forecast.get(i).getTempMorn());
            c.put(WeatherContract.WeatherEntry.COL_EVE_TEMP, forecast.get(i).getTempEve());
            c.put(WeatherContract.WeatherEntry.COL_NIGHT_TEMP, forecast.get(i).getTempNight());

            result[i] = c;
        }
        return result;
    }
}