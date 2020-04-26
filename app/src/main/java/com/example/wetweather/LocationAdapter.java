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
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.dateView.setText(WetWeatherUtils.getUpdateTime(mContext, weatherForThisDay.getDateTimeInSeconds()));
                holder.highTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.getTemperature()));
                holder.lowTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.apparentTemperature));

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

            case VIEW_TYPE_FUTURE_DAY:
                holder.entireDetailsLayout.setOnClickListener(new FutureDayClickHandler(weatherForThisDay.getDateTimeInSeconds()));
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());
                holder.dateView.setText(WetWeatherUtils.getDayName(mContext, weatherForThisDay.getDateTimeInSeconds()));
                holder.highTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureHigh));
                holder.lowTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureLow));

                break;

            case VIEW_TYPE_INFO:
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity());

                int infoType = weatherForThisDay.weatherType;
                String description = "";

                if (infoType == 6){
                    description = mContext.getString(R.string.next_7_days_label);
                } else if (infoType == 7){
                    description = mContext.getString(R.string.next_24_hours_label);
                    holder.entireDetailsLayout.setOnClickListener(new HourlyClickHandler());
                } else if (infoType == 8){
                    description = mContext.getString(R.string.next_hour_label);
                }

                holder.dateView.setText(description);

                break;

            case VIEW_TYPE_ALERTS:
                holder.entireDetailsLayout.setOnClickListener(new AlertClickHandler());
                weatherImageId = R.drawable.ic_circle_warning;
                holder.dateView.setText(String.format("%1$s %2$s", mContext.getString(R.string.alerts_label),
                        weatherForThisDay.getIcon()));
                holder.descriptionView.setSingleLine(true);
                holder.descriptionView.setEllipsize(TextUtils.TruncateAt.END);
                holder.descriptionView.setText(weatherForThisDay.getSummary());

                //set background color
                View root = holder.dateView.getRootView();
                //root.setBackgroundColor(mContext.getColor(R.color.warning_color));
                root.setBackgroundColor(Color.RED);
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

        LocationAdapterViewHolder(View view) {
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

