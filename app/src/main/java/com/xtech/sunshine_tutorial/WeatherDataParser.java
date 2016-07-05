package com.xtech.sunshine_tutorial;

import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherDataParser {
    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static ArrayList<String> jsonToForecastArrayList(String weatherJsonStr)
            throws JSONException {

        ArrayList<String> forecastList = new ArrayList<String>();
        JSONObject reader = new JSONObject(weatherJsonStr);
        JSONArray list = reader.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {

            JSONObject dayInfos = (JSONObject) list.get(i);

            JSONObject temp = dayInfos.getJSONObject("temp");
            String max = temp.getString("max");

            JSONArray weather = dayInfos.getJSONArray("weather");
            String main = weather.getJSONObject(0).getString("main");

            String day = WeatherDataParser.getCurrentDate();
            forecastList.add(new Forecast(day, main, max).toString());
        }

        return forecastList;
    }

    public static String getCurrentDate(){
        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        DateFormat df = new SimpleDateFormat("dd MM yyyy");
        String date = df.format(Calendar.getInstance().getTime());

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        switch(currentDayOfWeek){
            case 0:
                date += " - Mon";
                break;
            case 1:
                date += " - Tue";
                break;
            case 2:
                date += " - Wed";
                break;
            case 3:
                date += " - Thr";
                break;
            case 4:
                date += " - Fri";
                break;
            case 5:
                date += " - Sat";
                break;
            case 6:
                date += " - Sun";
                break;
        }

        return date;

    }
}
