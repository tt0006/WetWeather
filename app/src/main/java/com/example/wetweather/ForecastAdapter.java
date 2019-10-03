package com.example.wetweather;

import android.content.Context;
import android.graphics.Color;
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

class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_INFO = 2;
    private static final int VIEW_TYPE_ALERTS = 3;
    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;
    private final ForecastAdapterOnClickHandler mClickHandler;
    public boolean presentAlert;
    private List<WeatherItem> mWeatherData;

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ForecastAdapter.ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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

        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ForecastAdapterViewHolder holder, int position) {
        WeatherItem weatherForThisDay = mWeatherData.get(position);

        int viewType = getItemViewType(position);

        //set icon
        int weatherImageId;

        switch (viewType) {

            case VIEW_TYPE_TODAY:
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon());
                holder.dateView.setText(WetWeatherUtils.getUpdateTime(mContext, weatherForThisDay.getDateTimeMillis()));
                holder.highTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.getTemperature()));
                holder.lowTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.apparentTemperature));

                break;

            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon());
                holder.dateView.setText(WetWeatherUtils.getDayName(mContext, weatherForThisDay.getDateTimeMillis()));
                holder.highTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureHigh));
                holder.lowTempView.setText(WetWeatherUtils.formatTemperature(mContext, weatherForThisDay.temperatureLow));

                break;

            case VIEW_TYPE_INFO:
                weatherImageId = WetWeatherUtils
                        .getResourceIconIdForWeatherCondition(weatherForThisDay.getIcon());

                if (presentAlert) {
                    if (position == 2) {
                        holder.dateView.setText(mContext.getString(R.string.next_hour_label));
                    } else if (position == 3) {
                        holder.dateView.setText(mContext.getString(R.string.next_24_hours_label));
                    } else if (position == 4) {
                        holder.dateView.setText(mContext.getString(R.string.next_7_days_label));
                    }
                } else {

                    if (position == 1) {
                        holder.dateView.setText(mContext.getString(R.string.next_hour_label));
                    } else if (position == 2) {
                        holder.dateView.setText(mContext.getString(R.string.next_24_hours_label));
                    } else if (position == 3) {
                        holder.dateView.setText(mContext.getString(R.string.next_7_days_label));
                    }
                }
                break;

            case VIEW_TYPE_ALERTS:
                weatherImageId = R.drawable.ic_circle_warning;
                holder.dateView.setText(String.format("%1$s %2$s", mContext.getString(R.string.alerts_label),
                        weatherForThisDay.getIcon()));
                holder.descriptionView.setText(weatherForThisDay.getSummary());

                //set bacground color
                View root = holder.dateView.getRootView();
                //root.setBackgroundColor(mContext.getColor(R.color.warning_color));
                root.setBackgroundColor(Color.RED);
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

        WeatherItem weatherForThisDay = mWeatherData.get(position);

        if (position == 0 && weatherForThisDay.weatherType == 0) {
            presentAlert = true;
            return VIEW_TYPE_ALERTS;
        }

        if (presentAlert) {
            if (position == 2 || position == 3 || position == 4) {
                return VIEW_TYPE_INFO;
            } else if (position == 1) {
                return VIEW_TYPE_TODAY;
            } else {
                return VIEW_TYPE_FUTURE_DAY;
            }
        }

        if (position == 0) {
            return VIEW_TYPE_TODAY;
        } else if (position == 1 || position == 2 || position == 3) {
            return VIEW_TYPE_INFO;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    public void setWeatherData(List<WeatherItem> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }


    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(int position);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = view.findViewById(R.id.weather_icon);
            dateView = view.findViewById(R.id.date);
            descriptionView = view.findViewById(R.id.weather_description);
            highTempView = view.findViewById(R.id.high_temperature);
            lowTempView = view.findViewById(R.id.low_temperature);

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
}
