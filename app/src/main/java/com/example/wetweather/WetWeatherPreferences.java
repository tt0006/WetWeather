package com.example.wetweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public final class WetWeatherPreferences {

    public static String getPreferencesUpdateInterval(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUpdateInterval = context.getString(R.string.pref_update_interval_key);
        String defaultUpdateInterval = context.getString(R.string.pref_update_interval_value_0);
        String preferredUpdateInterval = sp.getString(keyForUpdateInterval, defaultUpdateInterval);

        return preferredUpdateInterval;
    }

    public static String getPreferencesUnits(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_auto);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);

        return preferredUnits;
    }

    public static String getPreferencesLanguage(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_en_value);

        return sp.getString(keyForLanguage, defaultLanguage);
    }
}
