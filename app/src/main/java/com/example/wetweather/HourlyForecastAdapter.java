package com.example.wetweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WPOpenWeather;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastAdapterViewHolder> {
    private static final String LOG_TAG = HourlyForecastAdapter.class.getName();
    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    private List<WeatherItem> mWeatherData;


    /**
     * Creates a LocationAdapter.
     */
    HourlyForecastAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.HourlyForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_hourly, parent, false);
        view.setFocusable(true);
        return new HourlyForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.HourlyForecastAdapterViewHolder holder, int position) {

        WeatherItem weatherForThisDay = mWeatherData.get(position);

        holder.entireHourlyLayout.setOnClickListener(new HourlyClickHandler(position, weatherForThisDay));

        // Get the state
        boolean expanded = weatherForThisDay.isExpanded();
        // Set the visibility based on state
        holder.hourlyChildLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.hourlyWeatherIcon.setImageResource(WetWeatherUtils.getResourceIconIdForWeatherCondition(
                mContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity()));
        holder.hourlyWeatherDescription.setText(weatherForThisDay.getSummary());
        holder.hourlyDate.setText(WetWeatherUtils.getHourWithDay(weatherForThisDay.getDateTimeInSeconds()));
        holder.hourlyTemperature.setText(WetWeatherUtils.formatTemperature(mContext,
                weatherForThisDay.getTemperature()));
        holder.hourlyRain.setText(String.format("%1$s %2$s",
                WetWeatherUtils.formatPrecipitationProbability(mContext, weatherForThisDay.getPrecipProbability()),
                WetWeatherUtils.formatPrecipitationIntensity(mContext, weatherForThisDay.getPrecipIntensity())));

        holder.hourlyWindIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
        holder.hourlyWindSpeed.setText(WetWeatherUtils.formatWindString(mContext, weatherForThisDay.getWindSpeed()));
        holder.hourlyWindGust.setText(WetWeatherUtils.formatWindString(mContext, weatherForThisDay.getWindGust()));

        holder.hourlyClouds.setText(mContext.getString(R.string.format_percent_value,
                weatherForThisDay.getCloudCover() * 100));

        holder.hourlyHumidity.setText(mContext.getString(R.string.format_percent_value,
                weatherForThisDay.getHumidity() * 100));

        holder.hourlyPressure.setText(mContext.getString(R.string.format_pressure,
                weatherForThisDay.getPressure()));
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item.
     */
    class HourlyForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        final View entireHourlyLayout, hourlyChildLayout;
        final ImageView hourlyWeatherIcon, hourlyWindIcon;

        final TextView hourlyDate, hourlyWeatherDescription, hourlyTemperature, hourlyRain,
        hourlyClouds, hourlyWindSpeed, hourlyWindGust, hourlyHumidity, hourlyPressure;

        HourlyForecastAdapterViewHolder(View view) {
            super(view);

            entireHourlyLayout = view.findViewById(R.id.entire_hourly_layout);
            hourlyChildLayout = view.findViewById(R.id.hourly_details_child_layout);

            hourlyWeatherIcon = view.findViewById(R.id.hourly_weather_icon);
            hourlyDate = view.findViewById(R.id.hourly_date);
            hourlyWeatherDescription = view.findViewById(R.id.hourly_weather_description);
            hourlyTemperature = view.findViewById(R.id.hourly_temperature);
            hourlyRain = view.findViewById(R.id.hourly_rain_value);
            hourlyClouds = view.findViewById(R.id.hourly_clouds_value);
            hourlyWindIcon = view.findViewById(R.id.hourly_wind_icon);
            hourlyWindSpeed = view.findViewById(R.id.hourly_wind_speed);
            hourlyWindGust = view.findViewById(R.id.hourly_wind_gust);
            hourlyHumidity = view.findViewById(R.id.hourly_humidity_value);
            hourlyPressure = view.findViewById(R.id.hourly_pressure_value);
        }
    }

    void setWeatherData(List<WeatherItem> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    class HourlyClickHandler implements View.OnClickListener{
        int mPosition;
        WeatherItem mWeatherForThisDay;

        HourlyClickHandler(int position, WeatherItem weatherForThisDay){
            mPosition = position;
            mWeatherForThisDay = weatherForThisDay;
        }

        @Override
        public void onClick(View v) {
            // Get the current state of the item
            boolean expanded = mWeatherForThisDay.isExpanded();
            // Change the state
            mWeatherForThisDay.setExpanded(!expanded);
            // Notify the adapter that item has changed
            HourlyForecastAdapter.this.notifyItemChanged(mPosition);
        }
    }
}
