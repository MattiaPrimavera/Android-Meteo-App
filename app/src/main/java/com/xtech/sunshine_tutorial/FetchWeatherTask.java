package com.xtech.sunshine_tutorial;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class FetchWeatherTask extends AsyncTask<String, Void, ArrayList<Forecast>> {
    private CustomWeatherAdapter adapter;
    private final Context mContext;

    public FetchWeatherTask(Context context, CustomWeatherAdapter adapter){
        this.mContext = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPostExecute(ArrayList<Forecast> result) {
        //Updating the list adapter
        if(result != null){
            this.adapter.clear();
            for(Forecast f : result){
                this.adapter.add(f);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Forecast> doInBackground(String... params) {
        final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        String format = "json";
        String units = "metric";
        int numDays = 7;

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q"; // Postal Code
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, ApiKey.getAppId())
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.d(LOG_TAG, forecastJsonStr);

            try{
                ArrayList<Forecast> forecastArray = WeatherDataParser.jsonToForecastArrayList(forecastJsonStr);
                // add to database
                if ( forecastArray.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[forecastArray.size()];
                    forecastArray.toArray(cvArray);
                    mContext.getContentResolver().bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, cvArray);
                }

                Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(forecastArray.get(0).getCity(), WeatherDataParser.getCurrentDayString(0));

                Cursor cur = mContext.getContentResolver().query(weatherForLocationUri, null, null, null, null);

                Vector<ContentValues> vector  = new Vector<ContentValues>(cur.getCount());
                if ( cur.moveToFirst() ) {
                    do {
                        ContentValues cv = new ContentValues();
                        DatabaseUtils.cursorRowToContentValues(cur, cv);
                        vector.add(cv);
                    } while (cur.moveToNext());
                }
                return forecastArray;
            }catch(JSONException e){ e.printStackTrace(); }

            try {
                Log.d("FINAL DATA PARSED: ", WeatherDataParser.jsonToForecastArrayList(forecastJsonStr).get(0).toString());
            }catch(JSONException e){
                Log.d("PARSING:", "error parsing json returned from server");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param cityName A human-readable city name
     * @return the row ID of the added location.
     */
    long addLocation(String cityName) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COL_CITY_NAME + " = ?",
                null,
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(WeatherContract.LocationEntry.COL_CITY_NAME, cityName);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    WeatherContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }
}