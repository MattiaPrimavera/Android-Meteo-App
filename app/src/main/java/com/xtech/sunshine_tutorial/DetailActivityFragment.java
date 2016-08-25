package com.xtech.sunshine_tutorial;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

    private ImageView detailIconView;
    private TextView detailDayStringView;
    private TextView detailCityView;
    private TextView detailMainView;
    private TextView detailMaxTempView;
    private TextView detailMinTempView;
    private TextView detailMornTempView;
    private TextView detailEveTempView;
    private TextView detailNightTempView;
    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        String locationSetting = Utility.getPreferredLocation(getActivity());

        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocation(
                locationSetting);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, null);

        this.adapter = new ForecastAdapter(getActivity(), cur, 0);

        this.detailIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        this.detailDayStringView = (TextView) rootView.findViewById(R.id.detail_day);
        this.detailMainView = (TextView) rootView.findViewById(R.id.detail_main);
        this.detailMaxTempView = (TextView) rootView.findViewById(R.id.detail_max);
        this.detailMinTempView = (TextView) rootView.findViewById(R.id.detail_min);
        this.detailMornTempView = (TextView) rootView.findViewById(R.id.detail_morn);
        this.detailEveTempView = (TextView) rootView.findViewById(R.id.detail_eve);
        this.detailNightTempView = (TextView) rootView.findViewById(R.id.detail_night);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        if (data != null && data.moveToFirst()){
            new DownloadIconTask(detailIconView).execute("http://openweathermap.org/img/w/" + data.getString(COL_WEATHER_ICON) + ".png");
//            detailCityView.setText("Paris");
            detailMainView.setText(data.getString(COL_WEATHER_DESC));
            detailMaxTempView.setText(data.getString(COL_WEATHER_MAX_TEMP));
            detailMinTempView.setText(data.getString(COL_WEATHER_MIN_TEMP));
            detailMornTempView.setText(data.getString(COL_WEATHER_MORN_TEMP));
            detailEveTempView.setText(data.getString(COL_WEATHER_EVE_TEMP));
            detailNightTempView.setText(data.getString(COL_WEATHER_NIGHT_TEMP));
            detailDayStringView.setText(data.getString(COL_WEATHER_DAY));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}