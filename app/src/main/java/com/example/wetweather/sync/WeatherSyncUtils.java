package com.example.wetweather.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * Helper class to initialize and update weather data for app
 */
public class WeatherSyncUtils {
    private static boolean sInitialized;

    public static void initialize(@NonNull final Context context) {
        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        startImmediateSync(context);
    }

    /**
     * Helper method to perform a sync immediately using a JobIntentService
     *
     * @param context Android Context used to create an intent and enqueue work with it
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, WeatherSyncJobIntentService.class);
        WeatherSyncJobIntentService.enqueueWork(context, intentToSyncImmediately);
    }
}