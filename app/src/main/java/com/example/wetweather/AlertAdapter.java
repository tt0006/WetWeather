package com.example.wetweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertAdapterViewHolder> {

    private final Context mContext;

    private List<WeatherItem> mWeatherData;

    /**
     * Creates an AlertAdapter.
     */
    AlertAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public AlertAdapter.AlertAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_alert, parent, false);
        view.setFocusable(true);

        return new AlertAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapterViewHolder holder, int position) {
        WeatherItem alertItem = mWeatherData.get(position);

        holder.titleView.setText(alertItem.getAlertTitle());
        holder.descriptionView.setText(alertItem.getSummary().trim());

        String alertTime = String.format("Starts: %1$s, Ends: %2$s",
                WetWeatherUtils.getHourWithDay(mContext, alertItem.getAlertStartTime()),
                WetWeatherUtils.getHourWithDay(mContext, alertItem.getAlertEntTime()));
        holder.alertTime.setText(alertTime);

    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class AlertAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView titleView;
        final TextView descriptionView;
        final TextView alertTime;

        AlertAdapterViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.alert_header);
            descriptionView = view.findViewById(R.id.alert_description);
            alertTime = view.findViewById(R.id.alert_start_time);
        }
    }

    void setWeatherData(List<WeatherItem> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
