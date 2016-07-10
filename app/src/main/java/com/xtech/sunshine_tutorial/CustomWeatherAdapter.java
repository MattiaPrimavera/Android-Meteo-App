package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomWeatherAdapter extends ArrayAdapter<Forecast>{
    // View lookup cache
    private static class ViewHolder {
        TextView text;
    }

    public CustomWeatherAdapter(Context context, ArrayList<Forecast> forecast) {
        super(context, R.layout.list_item_forecast, forecast);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Forecast forecast = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_forecast, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.list_item_forecast_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.text.setText(forecast.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}