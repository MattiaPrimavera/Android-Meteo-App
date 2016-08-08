package com.xtech.sunshine_tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null) {
            String jsonForecast = intent.getDataString();
            ((TextView) rootView.findViewById(R.id.detail_day)).setText(jsonForecast);
/*
            String jsonForecast = intent.getStringExtra(Intent.EXTRA_TEXT);
            Forecast forecast = null;
            try {
                forecast = Forecast.fromJSON(jsonForecast);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new DownloadIconTask((ImageView)rootView.findViewById(R.id.detail_icon)).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
            ((TextView) rootView.findViewById(R.id.detail_day)).setText(forecast.getDayNumber());
            ((TextView) rootView.findViewById(R.id.detail_city)).setText(forecast.getCity());
            ((TextView) rootView.findViewById(R.id.detail_main)).setText(forecast.getMain());
            ((TextView) rootView.findViewById(R.id.detail_max)).setText(forecast.getTempMax());
            ((TextView) rootView.findViewById(R.id.detail_min)).setText(forecast.getTempMin());
            ((TextView) rootView.findViewById(R.id.detail_morn)).setText(forecast.getTempMorn());
            ((TextView) rootView.findViewById(R.id.detail_eve)).setText(forecast.getTempEve());
            ((TextView) rootView.findViewById(R.id.detail_night)).setText(forecast.getTempNight());
*/
        }
        return rootView;
    }

}