package com.xtech.sunshine_tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Utility {
    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

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

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param context Context to use for resource localization
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(Context context, int number) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"
        long dateInMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int currentJulianDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.setTimeInMillis(dateInMillis);
        int julianDay = calendar.get(Calendar.DAY_OF_YEAR);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(formatId, today, getFormattedMonthDay(context)));
        } else if ( julianDay < currentJulianDay + 7 ) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, number);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param number int representing the day of the week, 0 being today
     * @return
     */
    public static String getDayName(Context context, int number) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        if (number == 0) {
            return context.getString(R.string.today);
        } else if ( number == 1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(System.currentTimeMillis());
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(System.currentTimeMillis());
        return monthDayString;
    }
}