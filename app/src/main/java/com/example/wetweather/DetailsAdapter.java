package com.example.wetweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsAdapterViewHolder>{

    private static final int VIEW_TYPE_DAILY = 0;
    private static final int VIEW_TYPE_HOURLY = 1;
    private final Context mContext;
    private List<WeatherItem> mWeatherData;

    DetailsAdapter(Context context) {
        mContext = context;
    }
    @NonNull
    @Override
    public DetailsAdapter.DetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_DAILY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }

            case VIEW_TYPE_HOURLY: {
                layoutId = R.layout.list_item_hourly;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new DetailsAdapter.DetailsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.DetailsAdapterViewHolder holder, int position) {

        WeatherItem weatherForThisDay = mWeatherData.get(position);
        int viewType = getItemViewType(position);
        int weatherImageId;

        switch (viewType) {

            case VIEW_TYPE_DAILY:

                holder.entireDetailsLayout.setOnClickListener(new DetailsClickHandler(position, weatherForThisDay));

                // Get the state
                boolean expanded = weatherForThisDay.isExpanded();
                // Set the visibility based on state
                holder.entireDetailsSet.setVisibility(expanded ? View.VISIBLE : View.GONE);

                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(mContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.detailsWeatherIcon.setImageResource(weatherImageId);

                holder.detailsDate.setVisibility(View.GONE);
                holder.detailsWeatherDescription.setText(weatherForThisDay.getSummary());
                holder.detailsHighTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.getTemperatureHigh()));
                holder.detailsLowTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.getTemperatureLow()));

                holder.detailsWindIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
                holder.detailsWindSpeed.setText(WetWeatherUtils.formatWindString(mContext, weatherForThisDay.getWindSpeed()));

                holder.detailsWindGust.setText(WetWeatherUtils.formatWindString(mContext, weatherForThisDay.getWindGust()));

                holder.detailsRainProb.setText(WetWeatherUtils.formatPrecipitationProbability(mContext, weatherForThisDay.getPrecipProbability()));
                holder.detailsRainIntens.setText(WetWeatherUtils.formatPrecipitationIntensity(mContext, weatherForThisDay.getPrecipIntensity()));

                holder.detailsHumidity.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getHumidity() * 100));

                holder.detailsPressure.setText(mContext.getString(R.string.format_pressure,
                        weatherForThisDay.getPressure()));

                holder.detailsSunrise.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunriseTime()));
                holder.detailsSunset.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunsetTime()));
                holder.detailsClouds.setText(mContext.getString(R.string.format_percent_value, weatherForThisDay.getCloudCover() * 100));
                holder.detailsMoonPhaseString.setText(WetWeatherUtils.getMoonPhase(mContext, weatherForThisDay.getMoonPhase()));
                holder.detailsMoonIcon.setImageResource(WetWeatherUtils.getMoonPhaseIcon(weatherForThisDay.getMoonPhase()));
                holder.detailsVisibility.setText(mContext.getString(R.string.format_visibility, weatherForThisDay.getVisibility()));

                break;

            case VIEW_TYPE_HOURLY:

                holder.entireHourlyLayout.setOnClickListener(new DetailsClickHandler(position, weatherForThisDay));

                // Get the state
                boolean expanded1 = weatherForThisDay.isExpanded();
                // Set the visibility based on state
                holder.hourlyChildLayout.setVisibility(expanded1 ? View.VISIBLE : View.GONE);

                weatherImageId = WetWeatherUtils.getResourceIconIdForWeatherCondition(mContext,
                        weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.hourlyWeatherIcon.setImageResource(weatherImageId);

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
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }

    @Override
    public int getItemViewType(int position) {

        int view = 0;

        WeatherItem weatherForThisDay = mWeatherData.get(position);

        switch (weatherForThisDay.getWeatherType()){

            case 1: view = VIEW_TYPE_DAILY;
                break;

            case 2: view = VIEW_TYPE_HOURLY;
                break;
        }

        return view;
    }

    void setWeatherData(List<WeatherItem> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item.
     */
    static class DetailsAdapterViewHolder extends RecyclerView.ViewHolder {

        final View entireDetailsLayout, entireDetailsSet, entireHourlyLayout, hourlyChildLayout;

        final ImageView detailsWeatherIcon, detailsWindIcon, detailsMoonIcon, hourlyWeatherIcon,
                hourlyWindIcon;

        final TextView detailsDate, detailsWeatherDescription, detailsHighTemp,
                detailsLowTemp, detailsRainProb, detailsRainIntens, detailsWindSpeed,
                detailsWindGust, detailsHumidity, detailsPressure, detailsSunrise, detailsSunset,
                detailsClouds, detailsMoonPhaseString, detailsVisibility,
                hourlyDate, hourlyWeatherDescription, hourlyTemperature, hourlyRain, hourlyClouds,
                hourlyWindSpeed, hourlyWindGust, hourlyHumidity, hourlyPressure;


        DetailsAdapterViewHolder(View view) {
            super(view);

            entireDetailsLayout = view.findViewById(R.id.entire_details_layout);
            entireDetailsSet = view.findViewById(R.id.extra_details_set);

            detailsWeatherIcon = view.findViewById(R.id.details_weather_icon);
            detailsDate = view.findViewById(R.id.details_date);
            detailsWeatherDescription = view.findViewById(R.id.details_weather_description);
            detailsHighTemp = view.findViewById(R.id.details_high_temperature);
            detailsLowTemp = view.findViewById(R.id.details_low_temperature);
            detailsRainProb = view.findViewById(R.id.details_rain_probability);
            detailsRainIntens = view.findViewById(R.id.details_rain_intensity);
            detailsWindIcon = view.findViewById(R.id.details_wind_icon);
            detailsWindSpeed = view.findViewById(R.id.details_wind_speed);
            detailsWindGust = view.findViewById(R.id.details_wind_gust);
            detailsHumidity = view.findViewById(R.id.details_humidity_value);
            detailsPressure = view.findViewById(R.id.details_pressure_value);
            detailsSunrise = view.findViewById(R.id.details_sunrise_value);
            detailsSunset = view.findViewById(R.id.details_sunset_value);
            detailsClouds = view.findViewById(R.id.details_clouds_value);
            detailsMoonPhaseString = view.findViewById(R.id.details_moon_phase_string_value);
            detailsMoonIcon = view.findViewById(R.id.details_moon_phase_icon);
            detailsVisibility = view.findViewById(R.id.details_visibility_value);

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

    class DetailsClickHandler implements View.OnClickListener{
        int mPosition;
        WeatherItem mWeatherForThisDay;

        DetailsClickHandler(int position, WeatherItem weatherForThisDay){
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
            DetailsAdapter.this.notifyItemChanged(mPosition);
        }
    }
}


