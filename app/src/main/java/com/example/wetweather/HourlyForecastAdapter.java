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
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_INFO = 0;
    private static final int VIEW_TYPE_HOURLY = 1;

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    private final HourlyForecastAdapterOnClickHandler mClickHandler;
    private List<WeatherItem> mWeatherData;

    /**
     * The interface that receives onClick messages.
     */
    public interface HourlyForecastAdapterOnClickHandler {
        void onClick(int position);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public HourlyForecastAdapter(Context context, HourlyForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.HourlyForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId;

        switch (viewType) {

            case VIEW_TYPE_INFO: {
                layoutId = R.layout.forecast_list_item;
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

        return new HourlyForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.HourlyForecastAdapterViewHolder holder, int position) {
        WeatherItem weatherForThisDay = mWeatherData.get(position);
        Log.i("!!!", weatherForThisDay.getSummary());

        int viewType = getItemViewType(position);

        //set icon
        int weatherImageId;

        switch (viewType) {

            case VIEW_TYPE_HOURLY:
                weatherImageId = WetWeatherUtils
                        .getResourceIdForWeatherCondition(weatherForThisDay.getIcon());
                holder.dateView.setText(WetWeatherUtils.getHourWithDay(mContext,
                        weatherForThisDay.getDateTimeMillis()));
                holder.tempView.setText(WetWeatherUtils.formatTemperature(mContext,
                        weatherForThisDay.getTemperature()));
                holder.rainProb.setText(String.format("%1$s %2$s",mContext.getString(
                        R.string.hourly_rain_prob_label), mContext.getString(R.string.format_percent_value,
                        weatherForThisDay.getPrecipProbability()*100)));

                break;

            case VIEW_TYPE_INFO:
                weatherImageId = WetWeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherForThisDay.getIcon());
                if (position == 0) {
                    holder.dateView.setText(mContext.getString(R.string.next_24_hours_label));
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        holder.iconView.setImageResource(weatherImageId);

        //set description
        holder.descriptionView.setText(weatherForThisDay.getSummary());
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == -1) {
            return VIEW_TYPE_INFO;
        } else {
            return VIEW_TYPE_HOURLY;
        }
    }


    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class HourlyForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView tempView;
        final TextView rainProb;

        HourlyForecastAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.hourly_weather_icon);
            dateView = view.findViewById(R.id.hourly_date);
            descriptionView = view.findViewById(R.id.hourly_weather_description);
            tempView = view.findViewById(R.id.hourly_temperature);
            rainProb = view.findViewById(R.id.hourly_rain);

            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch position that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * position.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setWeatherData(List<WeatherItem> weatherData) {
        mWeatherData = weatherData;
        Log.i("!!!", "" + weatherData.size());
        notifyDataSetChanged();
    }
}
