package com.example.wetweather.sync;

import android.content.Context;
import com.example.wetweather.utils.NetworkUtils;

public class WeatherSyncTask {

    /**
     * Call updateWeatherData method in utils that performs the network request for updated weather,
     * parses the JSON from that request, and inserts the new weather information into db.
     *
     * @param context Used to access utility methods and Shared Preferences
     */
    synchronized public static void syncWeather(Context context) {
        NetworkUtils.updateWeatherData(context);
    }
}
