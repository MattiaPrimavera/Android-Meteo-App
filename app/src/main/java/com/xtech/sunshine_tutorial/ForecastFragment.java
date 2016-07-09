package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> adapter;
    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

        this.adapter = new ArrayAdapter<String>(
                getActivity(), //Current Context -> This fragment parent activity
                R.layout.list_item_forecast, //List item layout
                R.id.list_item_forecast_textview, //Text View to Populate
                weekForecast); //Forecast data


        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = (String) parent.getItemAtPosition(position);
                // Starting detail Activity passing forecast as EXTRA data
                Intent intent = ((Intent) new Intent(getActivity(), DetailActivity.class)).
                        putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return rootView;
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
            FetchWeatherTask task = new FetchWeatherTask(this.adapter);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

            ConnectivityManager connectivityManager = (ConnectivityManager) (getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                task.execute(location);
                return true;
            }
            else {
                Toast.makeText(getActivity(), "No Internet Connection Avaiable", Toast.LENGTH_SHORT).show();
                Log.d("Network ERROR: ", "Error Network connection not avaiable");
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

