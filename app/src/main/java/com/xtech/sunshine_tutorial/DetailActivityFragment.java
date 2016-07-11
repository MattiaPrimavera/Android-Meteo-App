package com.xtech.sunshine_tutorial;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String jsonForecast = intent.getStringExtra(Intent.EXTRA_TEXT);
            Forecast forecast = null;
            try {
                forecast = Forecast.fromJSON(jsonForecast);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new DownloadIconTask((ImageView)rootView.findViewById(R.id.detail_icon)).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
            ((TextView) rootView.findViewById(R.id.detail_day)).setText(forecast.getDayNumber());
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(forecast.getMain());
            ((TextView) rootView.findViewById(R.id.detail_temp)).setText(forecast.getTemp());

        }
        return rootView;
    }

}