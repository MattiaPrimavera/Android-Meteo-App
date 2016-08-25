package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private ForecastAdapter adapter;
    private static final int FORECAST_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COL_DATE,
            WeatherContract.WeatherEntry.COL_DESC,
            WeatherContract.WeatherEntry.COL_MAX_TEMP,
            WeatherContract.WeatherEntry.COL_MIN_TEMP,
            WeatherContract.LocationEntry.COL_CITY_NAME,
            WeatherContract.WeatherEntry.COL_WEATHER_ID,
            WeatherContract.WeatherEntry.COL_ICON,
            WeatherContract.WeatherEntry.COL_HUMIDITY,
            WeatherContract.WeatherEntry.COL_DAY,
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_WEATHER_ICON = 7;
    static final int COL_WEATHER_HUMIDITY = 8;
    static final int COL_WEATHER_DAY = 9;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<Forecast> weekForecast = new ArrayList<Forecast>();
        //this.adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview);

        String locationSetting = Utility.getPreferredLocation(getActivity());
        Log.d("TEST: ", "Fetching database for date : " + WeatherContract.getDateString(System.currentTimeMillis()));

        String date = WeatherContract.getDateString(System.currentTimeMillis());
        Uri weatherForLocationUriWithStartDate = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                locationSetting, date);

        Uri weatherForLocation = WeatherContract.WeatherEntry.buildWeatherLocation(
                locationSetting);

/*        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUriWithStartDate,
                null, null, null, null);
*/
        Cursor cur = getActivity().getContentResolver().query(weatherForLocation,
                null, null, null, null);

        this.adapter = new ForecastAdapter(getActivity(), cur, 0);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(WeatherContract.WeatherEntry.buildWeatherLocation(
                                    locationSetting));
                    startActivity(intent);
                }
            }
        });

/*        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast forecast = (Forecast) parent.getItemAtPosition(position);
                // Starting detail Activity passing forecast as EXTRA data
                Intent intent = ((Intent) new Intent(getActivity(), DetailActivity.class)).
                        putExtra(Intent.EXTRA_TEXT, forecast.toJSON());
                startActivity(intent);
            }
        });
        Log.d("getView:", "getView fin onCreateView");*/
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        String locationSetting = Utility.getPreferredLocation(getActivity());
//
//        // Sort order:  Ascending, by date.
//        String sortOrder = WeatherContract.WeatherEntry.COL_DATE;
//        Uri weatherForLocationUriWithStartDate = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
//                locationSetting, WeatherContract.getDateString(System.currentTimeMillis()));
//
//        return new CursorLoader(getActivity(),
//                weatherForLocationUriWithStartDate,
//                FORECAST_COLUMNS,
//                null,
//                null,
//                sortOrder);

        String locationSetting = Utility.getPreferredLocation(getActivity());

        Uri weatherForLocation = WeatherContract.WeatherEntry.buildWeatherLocation(
                locationSetting);

        Cursor cur = getActivity().getContentResolver().query(weatherForLocation, null, null, null, null);
        Log.d("TEST: Querying for today's date gives rows : ", Integer.toString(cur.getCount()));

//        // Checking if we already have weather info on that date, so we update
//        Uri weatherForLocationUriWithStartDate = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//        locationSetting, WeatherContract.getDateString(System.currentTimeMillis() - 3600 * 24 * 1000 * 5));
//
//
//        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUriWithStartDate, null, null, null, null);
//        Log.d("TEST: Querying for today's date gives rows : ", Integer.toString(cur.getCount()));

        return new CursorLoader(getActivity(),
                weatherForLocation,
                FORECAST_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateWeather();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateWeather(){
        FetchWeatherTask task = new FetchWeatherTask(getContext());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        ConnectivityManager connectivityManager = (ConnectivityManager) (getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            task.execute(location);
        }
        else {
            Toast.makeText(getActivity(), "No Internet Connection Avaiable", Toast.LENGTH_SHORT).show();
            Log.d("Network ERROR: ", "Error Network connection not avaiable");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low, String unitType) {

        if (unitType.equals(getString(R.string.pref_units_imperial))) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
            Log.d("UNIT LOG: ", "Unit type not found: " + unitType);
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.adapter.swapCursor(null);
    }
}

