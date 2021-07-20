package com.example.wetweather.sync;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.wetweather.prefs.WetWeatherPreferences;

import java.util.concurrent.TimeUnit;

/**
 * Helper class to Run Scheduled updates periodically
 */
public class ScheduledUpdateRun {

    /**
     * Method to create scheduled weather updates with provided update interval from preferences
     *
     * @param context Android Context to use with utility methods and access preferences
     */
    public static void scheduleWeatherUpdate(Context context) {

        String interval = WetWeatherPreferences.getPreferencesUpdateInterval(context);
        int repeatInterval;

        try {
            repeatInterval = Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            repeatInterval = 0;
        }

        if (repeatInterval == 0) { // Cancel update if user select so in preferences
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
