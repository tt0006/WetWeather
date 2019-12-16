package com.example.wetweather.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.wetweather.R;

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

    public static String getPreferencesLocationName(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);

        return sp.getString(keyForLocation, defaultLocation);
    }

    public static String getPreferencesLatitudeLongitude(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLatitude = context.getString(R.string.pref_latitude_key);
        String keyForLogitude = context.getString(R.string.pref_longitude_key);
        return sp.getString(keyForLatitude, "")+","+sp.getString(keyForLogitude, "");
    }

    public static void updateLocationName(Context context, String locName){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_location_key), locName);
        editor.apply();
    }

    public static void setLatitudeLongitude(Context context, double latitude, double longitude){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_latitude_key), String.valueOf(latitude));
        editor.putString(context.getString(R.string.pref_longitude_key), String.valueOf(longitude));
        editor.apply();
    }
}
