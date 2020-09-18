package com.example.wetweather.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.wetweather.utils.NetworkUtils;
import com.example.wetweather.utils.UpdateWeatherData;

/**
 * A {@link JobIntentService} subclass used by startImmediateSync method in {@link WeatherSyncUtils}
 * class for handling asynchronous task requests in a service on a separate handler thread.
 * Using JobIntentService instead of IntentService to fulfil background services restriction in Android 8
 */
public class WeatherSyncJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, WeatherSyncJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        syncWeather(getApplicationContext());
    }

    /**
     * Call updateWeatherData method in utils that performs the network request for updated weather,
     * parses the JSON from that request, and inserts the new weather information into db.
     *
     * @param context Used to access utility methods
     */
    private static void syncWeather(Context context) {
        UpdateWeatherData.getLatestData(context);
    }
}