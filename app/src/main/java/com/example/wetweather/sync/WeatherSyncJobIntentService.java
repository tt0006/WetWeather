package com.example.wetweather.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

/**
 * An {@link JobIntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread. Using JobIntentService instead of IntentService to
 * fulfil background services restriction in Android 8
 */
public class WeatherSyncJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1;
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, WeatherSyncJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        WeatherSyncTask.syncWeather(getApplicationContext());
    }
}