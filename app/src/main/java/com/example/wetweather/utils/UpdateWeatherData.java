package com.example.wetweather.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.wetweather.R;
import com.example.wetweather.WeatherRepository;
import com.example.wetweather.WeatherWidget;
import com.example.wetweather.WeatherWidget4x1;
import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.WetWeatherPreferences;

import java.io.IOException;
import java.util.List;

public final class UpdateWeatherData {

    private static final String LOG_TAG = UpdateWeatherData.class.getName();

    /**
     * Processing method which combines multiple actions:
     * make network request using getResponseFromHttpUrl method, parse response using
     * extractJSONresponse method amd insert new data to db using WeatherRepository class.
     *
     * @param context Android Context to access utility methods and Shared Preferences
     * @return true if success or false if not (used for scheduled updates worker class)
     */
    synchronized public static boolean getLatestData(Context context) {

        String latitude = WetWeatherPreferences.getPreferencesLatitude(context);
        if (latitude.length()<1){
            Log.i(LOG_TAG, "!!!latitude is null");
            return false;
        }
        String weatherProvider = WetWeatherPreferences.getPreferencesWeatherProvider(context);

        try {

            List<WeatherItem> weatherListArray = null;
            if (weatherProvider.equals(context.getString(R.string.pref_weather_provider_dark_sky_value))) {
                Log.i(LOG_TAG, "Dark Sky path");
                weatherListArray = WPDarkSky.fetchWeatherData(context);
            } else if (weatherProvider.equals(context.getString(R.string.pref_weather_provider_open_weather_value))) {
                Log.i(LOG_TAG, "OpenWeather path");
                weatherListArray = WPOpenWeather.fetchWeatherData(context);
            }

            if (weatherListArray == null){
                Log.i(LOG_TAG, "Weather data is null");
                return false;
            }

            WeatherRepository.getInstance(context).insertData(weatherListArray);

        } catch (IOException e) {
            /* Server probably invalid */
            e.printStackTrace();
            return false;
        }
        updateAllWidgets(context);
        return true;
    }

    /**
     * Helper method to update app widgets
     *
     * @param context Android Context to access system resources and utility methods
     */
    private static void updateAllWidgets(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
        if (appWidgetIds.length > 0) {
            new WeatherWidget().onUpdate(context, appWidgetManager, appWidgetIds);
        }

        int[] appWidgetIds4x1 = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget4x1.class));
        if (appWidgetIds4x1.length > 0) {
            new WeatherWidget4x1().onUpdate(context, appWidgetManager, appWidgetIds4x1);
        }

    }
}
