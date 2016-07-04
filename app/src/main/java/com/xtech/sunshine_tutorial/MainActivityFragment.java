package com.xtech.sunshine_tutorial;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Array;
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

        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
