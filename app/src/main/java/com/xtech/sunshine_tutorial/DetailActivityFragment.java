package com.xtech.sunshine_tutorial;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private ForecastAdapter adapter;
    private static final int DETAIL_LOADER = 0;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COL_DAY,
            WeatherContract.WeatherEntry.COL_DESC,
            WeatherContract.WeatherEntry.COL_MAX_TEMP,
            WeatherContract.WeatherEntry.COL_MIN_TEMP,
            WeatherContract.WeatherEntry.COL_ICON,
            WeatherContract.WeatherEntry.COL_DAY_TEMP,
            WeatherContract.WeatherEntry.COL_MORN_TEMP,
            WeatherContract.WeatherEntry.COL_EVE_TEMP,
            WeatherContract.WeatherEntry.COL_NIGHT_TEMP,
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DAY = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_ICON = 5;
    private static final int COL_WEATHER_DAY_TEMP = 6;
    private static final int COL_WEATHER_MORN_TEMP = 7;
    private static final int COL_WEATHER_EVE_TEMP = 8;
    private static final int COL_WEATHER_NIGHT_TEMP = 9;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();

        String locationSetting = Utility.getPreferredLocation(getActivity());

        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocation(
                locationSetting);

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, null);

        this.adapter = new ForecastAdapter(getActivity(), cur, 0);

        if(intent != null) {
            String jsonForecast = intent.getDataString();
            ((TextView) rootView.findViewById(R.id.detail_day)).setText(jsonForecast);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("TEST:", "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        new DownloadIconTask((ImageView) getView().findViewById(R.id.detail_icon)).execute("http://openweathermap.org/img/w/" + data.getString(COL_WEATHER_ICON) + ".png");
        ((TextView) getView().findViewById(R.id.detail_day)).setText(data.getString(COL_WEATHER_DAY));
        ((TextView) getView().findViewById(R.id.detail_city)).setText("Paris");
        ((TextView) getView().findViewById(R.id.detail_main)).setText(data.getString(COL_WEATHER_DESC));
        ((TextView) getView().findViewById(R.id.detail_max)).setText(data.getString(COL_WEATHER_MAX_TEMP));
        ((TextView) getView().findViewById(R.id.detail_min)).setText(data.getString(COL_WEATHER_MIN_TEMP));
        ((TextView) getView().findViewById(R.id.detail_morn)).setText(data.getString(COL_WEATHER_MORN_TEMP));
        ((TextView) getView().findViewById(R.id.detail_eve)).setText(data.getString(COL_WEATHER_EVE_TEMP));
        ((TextView) getView().findViewById(R.id.detail_night)).setText(data.getString(COL_WEATHER_NIGHT_TEMP));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}