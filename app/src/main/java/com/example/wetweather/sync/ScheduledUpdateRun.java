package com.example.wetweather.sync;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.wetweather.prefs.WetWeatherPreferences;

import java.util.concurrent.TimeUnit;

public class ScheduledUpdateRun {

    public static void scheduleWeatherUpdate(Context context){

        String interval = WetWeatherPreferences.getPreferencesUpdateInterval(context);
        int repeatInterval;

        try {
            repeatInterval = Integer.parseInt(interval);
        } catch (NumberFormatException e){
            repeatInterval = 0;
        }

        if (repeatInterval == 0){
            WorkManager.getInstance(context).cancelAllWork();
        } else {

            // Create a Constraints object that defines when the task should run
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest syncWeather =
                    new PeriodicWorkRequest.Builder(ScheduledUpdateWorker.class, repeatInterval, TimeUnit.HOURS)
                            .setConstraints(constraints)
                            .build();

            WorkManager.getInstance(context)
                    .enqueue(syncWeather);
        }
    }
}
