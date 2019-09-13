package com.example.wetweather.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class WeatherSyncUtils {
    private static boolean sInitialized;

    public static void initialize(@NonNull final Context context){
        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        startImmediateSync(context);
    }
    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, WeatherSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}