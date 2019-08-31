package com.example.wetweather.sync;

import android.content.Context;
import com.example.wetweather.utils.NetworkUtils;

public class WeatherSyncTask {

    synchronized public static void syncWeather(Context context) {
        NetworkUtils.updateWeatherData(context);
    }
}
