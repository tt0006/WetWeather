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
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon());
                //holder.dateView.setText(WetWeatherUtils.getDayName(mContext, weatherForThisDay.getDateTimeMillis()));
                holder.dateView.setVisibility(View.GONE);
                holder.highTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureHigh));
                holder.lowTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureLow));

                holder.windIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
                holder.windSpeedDetails.setText(mContext.getString(R.string.format_wind_speed,
                        weatherForThisDay.getWindSpeed()));
                holder.windGustDetails.setText(mContext.getString(R.string.format_wind_speed,
                        WetWeatherUtils.getDoubleFromString(weatherForThisDay.windGust)));

                holder.rainDetailsProb.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getPrecipProbability() * 100));
                holder.rainDetailsIntens.setText(mContext.getString(R.string.format_percip_intens,
                        weatherForThisDay.getPrecipIntensity()));

                holder.humidityDetailsValue.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getHumidity() * 100));

                holder.pressureDetailsValue.setText(mContext.getString(R.string.format_pressure,
                        weatherForThisDay.getPressure()));

                holder.sunrise.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunriseTime()));
                holder.sunset.setText(WetWeatherUtils.getTime(mContext, weatherForThisDay.getSunsetTime()));
                holder.moonPhaseStringValue.setText(WetWeatherUtils.getMoonPhase(mContext, weatherForThisDay.moonPhase));
                holder.moonIcon.setImageResource(WetWeatherUtils.getMoonPhaseIcon(weatherForThisDay.moonPhase));
                holder.visibilityView.setText(mContext.getString(R.string.format_visibility, weatherForThisDay.visibility));

                break;

            case VIEW_TYPE_HOURLY:

                holder.entireHourlyLayout.setOnClickListener(new DetailsClickHandler(position, weatherForThisDay));

                // Get the state
                boolean expanded1 = weatherForThisDay.isExpanded();
                // Set the visibility based on state
                holder.detailsHourlyChildLayout.setVisibility(expanded1 ? View.VISIBLE : View.GONE);

                weatherImageId = WetWeatherUtils.getResourceIconIdForWeatherCondition(
                        weatherForThisDay.getIcon());
                holder.dateView.setText(WetWeatherUtils.getHourWithDay(weatherForThisDay.getDateTimeMillis()));
                holder.tempView.setText(WetWeatherUtils.formatTemperature(mContext,
                        weatherForThisDay.getTemperature()));
                holder.rainProb.setText(String.format("%1$s %2$s", mContext.getString(
                        R.string.hourly_rain_prob_label),
                        mContext.getString(R.string.format_percent_value,
                                weatherForThisDay.getPrecipProbability() * 100)));

                holder.windIcon.setImageResource(WetWeatherUtils.getWindIcon(weatherForThisDay.getWindDirection()));
                holder.windSpeedDetails.setText(mContext.getString(R.string.format_wind_speed,
                        weatherForThisDay.getWindSpeed()));
                holder.windGustDetails.setText(mContext.getString(R.string.format_wind_speed,
                        WetWeatherUtils.getDoubleFromString(weatherForThisDay.windGust)));

                holder.rainDetailsProb.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getPrecipProbability() * 100));
                holder.rainDetailsIntens.setText(mContext.getString(R.string.format_percip_intens,
                        weatherForThisDay.getPrecipIntensity()));

                holder.humidityDetailsValue.setText(mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getHumidity() * 100));

                holder.pressureDetailsValue.setText(mContext.getString(R.string.format_pressure,
                        weatherForThisDay.getPressure()));
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        //set common
        holder.iconView.setImageResource(weatherImageId);
        holder.descriptionView.setText(weatherForThisDay.getSummary());
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
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        final View entireDetailsLayout;
        final View entireDetailsSet;

        final TextView rainDetailsProb;
        final TextView rainDetailsIntens;
        final ImageView windIcon;
        final TextView windSpeedDetails;
        final TextView windGustDetails;
        final TextView humidityDetailsValue;
        final TextView pressureDetailsValue;
        final TextView sunrise;
        final TextView sunset;
        final TextView moonPhaseStringValue;
        final ImageView moonIcon;
        final TextView visibilityView;

        final TextView tempView;
        final TextView rainProb;
        final View entireHourlyLayout;
        final View detailsHourlyChildLayout;

        DetailsAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            highTempView = view.findViewById(R.id.high_temperature);
            lowTempView = view.findViewById(R.id.low_temperature);

            entireDetailsLayout = view.findViewById(R.id.entire_details_layout);
            entireDetailsSet = view.findViewById(R.id.extra_details_set);

            rainDetailsProb = view.findViewById(R.id.rain_details_probability);
            rainDetailsIntens = view.findViewById(R.id.rain_details_intensity);

            windIcon = view.findViewById(R.id.wind_icon);
            windSpeedDetails = view.findViewById(R.id.wind_details_speed);
            windGustDetails = view.findViewById(R.id.wind_details_gust);

            humidityDetailsValue = view.findViewById(R.id.humidity_details_value);

            pressureDetailsValue = view.findViewById(R.id.pressure_details_value);

            sunrise = view.findViewById(R.id.sunrise_value);
            sunset = view.findViewById(R.id.sunset_value);
            moonPhaseStringValue = view.findViewById(R.id.moon_phase_string_value);
            moonIcon = view.findViewById(R.id.moon_phase_icon);
            visibilityView = view.findViewById(R.id.visibility_details_value);

            tempView = view.findViewById(R.id.hourly_temperature);
            rainProb = view.findViewById(R.id.hourly_rain);

            entireHourlyLayout = view.findViewById(R.id.entire_hourly_layout);

            detailsHourlyChildLayout = view.findViewById(R.id.details_hourly);
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


