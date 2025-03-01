package com.example.wetweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.WetWeatherPreferences;
import com.example.wetweather.sync.WeatherSyncUtils;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget4x1 extends AppWidgetProvider {

    private static WeatherItem weatherData;
    private static final String UPDATE_CLICKED = "com.example.wetweather.widgetUpdateIconClick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget4x1);

        //Define intent to start location activity from widget
        Intent startMainActivityIntent = new Intent(context, PlacesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startMainActivityIntent, 0);

        //Define intent to start sync when refresh button is clicked
        Intent startSyncIntent = new Intent(context, WeatherWidget.class);
        startSyncIntent.setAction(UPDATE_CLICKED);

        remoteViews.setOnClickPendingIntent(R.id.weather_icon, pendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.widget_update_icon, PendingIntent.getBroadcast(context, 0, startSyncIntent, 0));

        remoteViews.setTextViewText(R.id.location, String.format("%1$s %2$s",
                WetWeatherPreferences.getPreferencesLocationName(context),
                WetWeatherUtils.formatTemperature(context, weatherData.getTemperature())));

        remoteViews.setTextViewText(R.id.weather_description, weatherData.getSummary());

        remoteViews.setTextViewText(R.id.percip_prob, WetWeatherUtils.formatRainOrUVIndex(context,
                weatherData.getPrecipIntensity(), weatherData.getUvIndex()));
        remoteViews.setTextViewText(R.id.feels_like, String.format("%1$s %2$s", context.getString(R.string.feels_like_label),
                WetWeatherUtils.formatTemperature(context, weatherData.getApparentTemperature())));
        remoteViews.setImageViewResource(R.id.weather_icon, WetWeatherUtils.getResourceIconIdForWeatherCondition(context, weatherData.getIcon(), weatherData.getPrecipIntensity()));
        remoteViews.setTextViewText(R.id.widget_updated_at, WetWeatherUtils.getUpdateTime(context, weatherData.getDateTimeInSeconds()));


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        loadWeatherFromDB(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (UPDATE_CLICKED.equals(intent.getAction())){
            WeatherSyncUtils.startImmediateSync(context);
        }
    }

    //helper method to fetch data from db and call update widget method
    private static void loadWeatherFromDB(final Context context,
                                          final AppWidgetManager appWidgetManager,
                                          final int[] appWidgetIds){

        new AsyncTask<Context, Void, List<WeatherItem>>(){
            @Override
            protected List<WeatherItem> doInBackground(Context... context) {

                WeatherDB db = WeatherDB.getInstance(context[0]);
                return db.weatherDao().loadCurrentWeather();
            }

            @Override
            protected void onPostExecute(List<WeatherItem> list) {
                super.onPostExecute(list);
                weatherData = list.get(0);
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }.execute(context);
    }
}

