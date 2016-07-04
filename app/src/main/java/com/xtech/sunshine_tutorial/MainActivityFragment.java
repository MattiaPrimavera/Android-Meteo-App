package com.xtech.sunshine_tutorial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = {
                "Today - Sunny - 35°",
                "Tomorrow - Foggy - 20°",
                "Weds - Cloudy - 15°",
                "Thurs - Asteroids - 65°",
                "Fri - Heavy rain - 20°",
                "Sat - Storm - 10°",
                "Sun - Sunny - 40°"
        };

        List<String> weekForecast = new ArrayList<String>();
        for(String e : forecastArray){
            weekForecast.add(e);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), //Current Context -> This fragment parent activity
                R.layout.list_item_forecast, //List item layout
                R.id.list_item_forecast_textview, //Text View to Populate
                weekForecast); //Forecast data

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        return rootView;
    }
}
