package com.xtech.sunshine_tutorial;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private Forecast convertCursorRowToForecast(Cursor cursor) {
        // get row indices for our cursor
        int max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_MAX_TEMP);
        int min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_MIN_TEMP);
        int date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_DATE);
        int main = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_DESC);
        int humidity = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_HUMIDITY);
        int icon = cursor.getColumnIndex(WeatherContract.WeatherEntry.COL_ICON);
        String dayNumber = WeatherDataParser.getCurrentDayNumber(0);

        Forecast result = new Forecast(dayNumber, cursor.getString(date), cursor.getString(main), null, cursor.getString(icon), "", cursor.getString(humidity));
        result.setTempMin(cursor.getString(min_temp));
        result.setTempMax(cursor.getString(max_temp));

        return result;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Forecast forecast = convertCursorRowToForecast(cursor);

        TextView dayNumber = (TextView) view.findViewById(R.id.list_item_forecast_day_number);
        TextView dayString = (TextView) view.findViewById(R.id.list_item_forecast_day_string);
        TextView main = (TextView) view.findViewById(R.id.list_item_forecast_main);
        TextView day = (TextView) view.findViewById(R.id.list_item_forecast_day);

        new DownloadIconTask((ImageView) view.findViewById(R.id.weather_icon)).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
        main.setText(forecast.getMain());
        day.setText(forecast.getTempDay());
        dayNumber.setText(forecast.getDayNumber());
        dayString.setText(forecast.getDayString());
    }
}