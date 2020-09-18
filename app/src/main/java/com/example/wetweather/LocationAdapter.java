package com.example.wetweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
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

class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_INFO = 2;
    private static final int VIEW_TYPE_ALERTS = 3;
    private final Context mContext;
    private List<WeatherItem> mWeatherData;

    /**
     * Creates a LocationAdapter.
     */
    LocationAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public LocationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }

            case VIEW_TYPE_ALERTS:
            case VIEW_TYPE_INFO:
                layoutId = R.layout.list_item_info;
                break;

            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.forecast_list_item;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new LocationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapterViewHolder holder, int position) {

        WeatherItem weatherForThisDay = mWeatherData.get(position);
        int viewType = getItemViewType(position);
        int weatherImageId;

        switch (viewType) {

            case VIEW_TYPE_TODAY:
                holder.entireDetailsLayout.setOnClickListener(new TodayClickHandler(position, weatherForThisDay));

                // Get the state
                boolean expanded = weatherForThisDay.isExpanded();
                // Set the visibility based on state
                holder.entireDetailsSet.setVisibility(expanded ? View.VISIBLE : View.GONE);

                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(mContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.detailsWeatherIcon.setImageResource(weatherImageId);
                holder.detailsWeatherDescription.setText(weatherForThisDay.getSummary());
                holder.detailsDate.setText(WetWeatherUtils.getUpdateTime(mContext, weatherForThisDay.getDateTimeInSeconds()));
                holder.detailsHighTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.getTemperature()));
                holder.detailsLowTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.apparentTemperature));

                holder.detailsWindIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
                holder.detailsWindSpeed.setText(mContext.getString(R.string.format_wind_speed,
                        weatherForThisDay.getWindSpeed()));
                holder.detailsWindGust.setText(mContext.getString(R.string.format_wind_speed,
                        WetWeatherUtils.getDoubleFromString(weatherForThisDay.windGust)));

                holder.detailsRainProb.setText(mContext.getString(R.string.format_percent_value,
                        Float.parseFloat(weatherForThisDay.getPrecipProbability()) * 100));
                holder.detailsRainIntens.setText(mContext.getString(R.string.format_percip_intens,
                        Float.parseFloat(weatherForThisDay.getPrecipIntensity())));

                holder.detailsHumidity.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getHumidity() * 100));

                holder.detailsPressure.setText(mContext.getString(R.string.format_pressure,
                        weatherForThisDay.getPressure()));

                holder.detailsSunrise.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunriseTime()));
                holder.detailsSunset.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunsetTime()));
                holder.detailsClouds.setText(mContext.getString(R.string.format_percent_value, weatherForThisDay.cloudCover * 100));
                holder.detailsMoonPhaseString.setText(WetWeatherUtils.getMoonPhase(mContext, weatherForThisDay.moonPhase));
                holder.detailsMoonIcon.setImageResource(WetWeatherUtils.getMoonPhaseIcon(weatherForThisDay.moonPhase));
                holder.detailsVisibility.setText(mContext.getString(R.string.format_visibility, weatherForThisDay.visibility));

                break;

            case VIEW_TYPE_FUTURE_DAY:
                holder.entireForecastLayout.setOnClickListener(new FutureDayClickHandler(weatherForThisDay.getDateTimeInSeconds()));
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(mContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.forecastIcon.setImageResource(weatherImageId);
                holder.forecastWeatherDescription.setText(weatherForThisDay.getSummary());
                holder.forecastDate.setText(WetWeatherUtils.getDayName(mContext, weatherForThisDay.getDateTimeInSeconds()));
                holder.forecastHighTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureHigh));
                holder.forecasLowTemp.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureLow));

                break;

            case VIEW_TYPE_INFO:
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(mContext, weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.infoIcon.setImageResource(weatherImageId);

                int infoType = weatherForThisDay.weatherType;
                String description = "";

                if (infoType == 6){
                    description = mContext.getString(R.string.next_7_days_label);
                } else if (infoType == 7){
                    description = mContext.getString(R.string.next_24_hours_label);
                    holder.entireInfoLayout.setOnClickListener(new HourlyClickHandler());
                } else if (infoType == 8){
                    description = mContext.getString(R.string.next_hour_label);
                }

                holder.infoDate.setText(description);
                holder.infoWeatherDescription.setText(weatherForThisDay.getSummary());

                break;

            case VIEW_TYPE_ALERTS:
                holder.entireInfoLayout.setOnClickListener(new AlertClickHandler());
                weatherImageId = R.drawable.ic_circle_warning;
                holder.infoIcon.setImageResource(weatherImageId);
                holder.infoDate.setText(String.format("%1$s %2$s", mContext.getString(R.string.alerts_label),
                        weatherForThisDay.getIcon()));
                holder.infoWeatherDescription.setSingleLine(true);
                holder.infoWeatherDescription.setEllipsize(TextUtils.TruncateAt.END);
                holder.infoWeatherDescription.setText(weatherForThisDay.getSummary());

                //set background color
                View root = holder.infoDate.getRootView();
                //root.setBackgroundColor(mContext.getColor(R.color.warning_color));
                root.setBackgroundColor(Color.RED);
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

        switch (weatherForThisDay.weatherType){

            case 1: view = VIEW_TYPE_TODAY;
            break;

            case 3: view = VIEW_TYPE_FUTURE_DAY;
            break;

            case 5: view = VIEW_TYPE_ALERTS;
            break;

            case 6:
            case 7:
            case 8: view = VIEW_TYPE_INFO;
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
    static class LocationAdapterViewHolder extends RecyclerView.ViewHolder {

        final View entireDetailsLayout, entireDetailsSet, entireForecastLayout, entireInfoLayout;

        final ImageView infoIcon, forecastIcon, detailsWeatherIcon, detailsWindIcon, detailsMoonIcon;

        final TextView infoDate, infoWeatherDescription, forecastDate, forecastWeatherDescription,
                forecastHighTemp, forecasLowTemp,
                detailsDate, detailsWeatherDescription, detailsHighTemp,
                detailsLowTemp, detailsRainProb, detailsRainIntens, detailsWindSpeed,
                detailsWindGust, detailsHumidity, detailsPressure, detailsSunrise, detailsSunset,
                detailsClouds, detailsMoonPhaseString, detailsVisibility;

        LocationAdapterViewHolder(View view) {
            super(view);

            entireInfoLayout = view.findViewById(R.id.info_layout);
            infoIcon = view.findViewById(R.id.info_weather_icon);
            infoDate = view.findViewById(R.id.info_date);
            infoWeatherDescription = view.findViewById(R.id.info_weather_description);

            entireForecastLayout = view.findViewById(R.id.forecast_layout);
            forecastIcon = view.findViewById(R.id.forecast_weather_icon);
            forecastDate = view.findViewById(R.id.forecast_date);
            forecastWeatherDescription = view.findViewById(R.id.forecast_weather_description);
            forecastHighTemp = view.findViewById(R.id.forecast_high_temperature);
            forecasLowTemp = view.findViewById(R.id.forecast_low_temperature);

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
        }
    }

    //Helper click handler classes

    class TodayClickHandler implements View.OnClickListener{
        int mPosition;
        WeatherItem mWeatherForThisDay;

        TodayClickHandler(int position, WeatherItem weatherForThisDay){
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
            LocationAdapter.this.notifyItemChanged(mPosition);

        }
    }

    class FutureDayClickHandler implements View.OnClickListener{
        long mTime;

        FutureDayClickHandler(long time){
            mTime = time;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("TIME", mTime);
            mContext.startActivity(intent);
        }
    }

    class HourlyClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, HourlyForecastActivity.class);
            mContext.startActivity(intent);
        }
    }

    class AlertClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AlertActivity.class);
            mContext.startActivity(intent);
        }
    }
}

