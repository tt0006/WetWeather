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

class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastAdapterViewHolder> {

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
        holder.detailsHourlyChildLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.iconView.setImageResource(WetWeatherUtils.getResourceIconIdForWeatherCondition(
                weatherForThisDay.getIcon(), weatherForThisDay.getPrecipIntensity()));
        holder.descriptionView.setText(weatherForThisDay.getSummary());
        holder.dateView.setText(WetWeatherUtils.getHourWithDay(weatherForThisDay.getDateTimeInSeconds()));
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
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView tempView;
        final TextView rainProb;
        final View entireHourlyLayout;
        final View detailsHourlyChildLayout;
        final TextView rainDetailsProb;
        final TextView rainDetailsIntens;
        final ImageView windIcon;
        final TextView windSpeedDetails;
        final TextView windGustDetails;
        final TextView humidityDetailsValue;
        final TextView pressureDetailsValue;

        HourlyForecastAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            tempView = view.findViewById(R.id.hourly_temperature);
            rainProb = view.findViewById(R.id.hourly_rain);

            entireHourlyLayout = view.findViewById(R.id.entire_hourly_layout);

            detailsHourlyChildLayout = view.findViewById(R.id.details_hourly);

            rainDetailsProb = view.findViewById(R.id.rain_details_probability);
            rainDetailsIntens = view.findViewById(R.id.rain_details_intensity);

            windIcon = view.findViewById(R.id.wind_icon);
            windSpeedDetails = view.findViewById(R.id.wind_details_speed);
            windGustDetails = view.findViewById(R.id.wind_details_gust);

            humidityDetailsValue = view.findViewById(R.id.humidity_details_value);

            pressureDetailsValue = view.findViewById(R.id.pressure_details_value);
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
