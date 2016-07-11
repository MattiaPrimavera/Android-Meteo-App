package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomWeatherAdapter extends ArrayAdapter<Forecast>{
    // View lookup cache
    private static class ViewHolder {
        LinearLayout mainLayout;
        TextView text, main, temp, dayNumber, dayString, city;
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
            LayoutInflater inflater = LayoutInflater.from(getContext());
            switch (position) {
                case 0:
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.list_item_forecast_first, parent, false);
                    viewHolder.dayNumber = (TextView) convertView.findViewById(R.id.list_item_forecast_day_number_first);
                    viewHolder.dayString = (TextView) convertView.findViewById(R.id.list_item_forecast_day_string_first);
                    viewHolder.main = (TextView) convertView.findViewById(R.id.list_item_forecast_main_first);
                    viewHolder.temp = (TextView) convertView.findViewById(R.id.list_item_forecast_temp_first);
                    viewHolder.city = (TextView) convertView.findViewById(R.id.list_item_forecast_city_first);

                    convertView.setTag(viewHolder);
                    new DownloadIconTask((ImageView) convertView.findViewById(R.id.weather_icon_first)).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
                    viewHolder.city.setText(forecast.getCity());

                    viewHolder.temp.setTextSize(20);
                    viewHolder.main.setTextSize(50);
                    viewHolder.dayString.setTextSize(40);
                    viewHolder.dayNumber.setTextSize(30);
                    break;
                default:
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.list_item_forecast, parent, false);
                    viewHolder.dayNumber = (TextView) convertView.findViewById(R.id.list_item_forecast_day_number);
                    viewHolder.dayString = (TextView) convertView.findViewById(R.id.list_item_forecast_day_string);
                    viewHolder.main = (TextView) convertView.findViewById(R.id.list_item_forecast_main);
                    viewHolder.temp = (TextView) convertView.findViewById(R.id.list_item_forecast_temp);

                    convertView.setTag(viewHolder);
                    new DownloadIconTask((ImageView) convertView.findViewById(R.id.weather_icon)).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
                    // Populate the data into the template view using the data object
                    break;
            }
            viewHolder.temp.setText(forecast.getTemp());
            viewHolder.main.setText(forecast.getMain());
            viewHolder.dayNumber.setText(forecast.getDayNumber());
            viewHolder.dayString.setText(forecast.getDayString());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position % 2 != 0){
            viewHolder.mainLayout = (LinearLayout) convertView.findViewById(R.id.list_item_forecast_main_layout);
            viewHolder.mainLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}