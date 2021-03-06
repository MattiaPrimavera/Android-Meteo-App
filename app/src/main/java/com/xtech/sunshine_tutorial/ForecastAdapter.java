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
    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dayStringView;
        public final TextView mainView;
        public final TextView maxTempView;
        public final TextView minTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dayStringView = (TextView) view.findViewById(R.id.list_item_forecast_day_string);
            mainView = (TextView) view.findViewById(R.id.list_item_forecast_main);
            maxTempView = (TextView) view.findViewById(R.id.list_item_forecast_day_max);
            minTempView = (TextView) view.findViewById(R.id.list_item_forecast_day_min);
        }
    }

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private Forecast convertCursorRowToForecast(Cursor cursor) {
        // get row indices for our cursor
        int max_temp = ForecastFragment.COL_WEATHER_MAX_TEMP;
        int min_temp = ForecastFragment.COL_WEATHER_MIN_TEMP;
        int date = ForecastFragment.COL_WEATHER_DATE;
        int main = ForecastFragment.COL_WEATHER_DESC;
        int humidity = ForecastFragment.COL_WEATHER_HUMIDITY;
        int icon = ForecastFragment.COL_WEATHER_ICON;
        int dayNumber = ForecastFragment.COL_WEATHER_DAY;

        Forecast result = new Forecast(cursor.getInt(dayNumber), cursor.getString(date), cursor.getString(main), null, cursor.getString(icon), "", cursor.getString(humidity), System.currentTimeMillis());
        result.setTempMin(cursor.getString(min_temp));
        result.setTempMax(cursor.getString(max_temp));

        return result;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_first;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Forecast forecast = convertCursorRowToForecast(cursor);
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        new DownloadIconTask((ImageView) viewHolder.iconView).execute("http://openweathermap.org/img/w/" + forecast.getIconName() + ".png");
        int dayNumberInt = forecast.getDayNumber();
        String formattedDayString = Utility.getFriendlyDayString(context, dayNumberInt);
        viewHolder.mainView.setText(forecast.getMain());
        viewHolder.maxTempView.setText(forecast.getTempMax());
        viewHolder.minTempView.setText(forecast.getTempMin());
        viewHolder.dayStringView.setText(formattedDayString);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }
}