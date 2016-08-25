package com.xtech.sunshine_tutorial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WeatherDataParser {
    /**
     * Return Weather information as an Array of Forecast Objects using the API
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static ArrayList<Forecast> jsonToForecastArrayList(String weatherJsonStr)
            throws JSONException {

        ArrayList<Forecast> forecastList = new ArrayList<Forecast>();
        JSONObject reader = new JSONObject(weatherJsonStr);
        JSONObject city = reader.getJSONObject("city");
        String cityName = city.getString("name");
        JSONArray list = reader.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject dayInfos = (JSONObject) list.get(i);
            String humidity = dayInfos.getString("humidity");
            JSONObject temp = dayInfos.getJSONObject("temp");
            HashMap<String, String> temperature = new HashMap<String, String>();
            temperature.put("day", temp.getString("day"));
            temperature.put("min", temp.getString("min"));
            temperature.put("max", temp.getString("max"));
            temperature.put("night", temp.getString("night"));
            temperature.put("eve", temp.getString("eve"));
            temperature.put("morn", temp.getString("morn"));

            JSONArray weather = dayInfos.getJSONArray("weather");
            String main = weather.getJSONObject(0).getString("main");
            String iconName = weather.getJSONObject(0).getString("icon");

            String dayString = WeatherDataParser.getCurrentDayString(i);

            forecastList.add(new Forecast(i, dayString, main, temperature, iconName, cityName, humidity, System.currentTimeMillis()));
        }

        return forecastList;
    }

    public static String getCurrentDayNumber(int shift) {
        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, shift); // Adding 5 days
        String output = sdf.format(c.getTime());
        return output;
    }

    public static String getCurrentDayString(int shift){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, shift - 2);
        int currentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        switch(currentDayOfWeek % 7){
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thr";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";
            default:
                return "Err";
        }
    }
}
