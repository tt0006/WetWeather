package com.example.wetweather.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.wetweather.R;

/**
 * Helper class to read and set preferences for app
 */
public final class WetWeatherPreferences {

    /**
     * Returns the update interval currently set in Preferences. The default is 0, which means no
     * need to automatically update weather
     *
     * @param context Context used to access SharedPreferences
     * @return update interval as String
     */
    public static String getPreferencesUpdateInterval(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUpdateInterval = context.getString(R.string.pref_update_interval_key);
        String defaultUpdateInterval = context.getString(R.string.pref_update_interval_value_0);
        String preferredUpdateInterval = sp.getString(keyForUpdateInterval, defaultUpdateInterval);

        return preferredUpdateInterval;
    }

    /**
     * Returns the units currently set in Preferences. The default is "auto", which means server
     * returns data in units default to location (based on latitude and longitude in request)
     *
     * @param context Context used to access SharedPreferences
     * @return units as String
     */
    public static String getPreferencesUnits(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_auto);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);

        return preferredUnits;
    }

    /**
     * Returns the language currently set in Preferences. The default is "en" for English.
     * Language is used as parameter in request for server to return result in specified language.
     *
     * @param context Context used to access SharedPreferences
     * @return language as String
     */
    public static String getPreferencesLanguage(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLanguage = context.getString(R.string.pref_language_key);
        String defaultLanguage = context.getString(R.string.pref_language_en_value);

        return sp.getString(keyForLanguage, defaultLanguage);
    }

    /**
     * Returns the location name set in Preferences. The default is "Cork".
     * Location ame is displayed in app and widget
     *
     * @param context Context used to access SharedPreferences
     * @return location as String
     */
    public static String getPreferencesLocationName(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);

        return sp.getString(keyForLocation, defaultLocation);
    }

    /**
     * Returns latitude and longitude set in Preferences. The default is empty string "".
     * Used as parameter in request for server to return result for specified location.
     *
     * @param context Context used to access SharedPreferences
     * @return latitude and longitude as String
     */
    public static String getPreferencesLatitudeLongitude(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLatitude = context.getString(R.string.pref_latitude_key);
        String keyForLogitude = context.getString(R.string.pref_longitude_key);
        return sp.getString(keyForLatitude, "")+","+sp.getString(keyForLogitude, "");
    }

    /**
     * Update location name in Preferences. In case user wants to rename the location.
     *
     * @param context Context used to access SharedPreferences
     * @param locName String with new name
     */
    public static void updateLocationName(Context context, String locName){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_location_key), locName);
        editor.apply();
    }

    /**
     * Set latitude and longitude in Preferences.
     *
     * @param context Context used to access SharedPreferences
     * @param latitude double latitude value
     * @param longitude double longitude value
     */
    public static void setLatitudeLongitude(Context context, double latitude, double longitude){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_latitude_key), String.valueOf(latitude));
        editor.putString(context.getString(R.string.pref_longitude_key), String.valueOf(longitude));
        editor.apply();
    }
}
