package com.example.wetweather;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.sync.WeatherSyncUtils;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    private static WeatherItem weatherData;
    private static final String UPDATE_CLICKED = "com.example.wetweather.widgetUpdateIconClick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Define intent to start main activity from widget
        Intent startMainActivityIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startMainActivityIntent, 0);

        //Define intent to start sync whhen refresh button is clicked
        Intent startSyncIntent = new Intent(context, WeatherWidget.class);
        startSyncIntent.setAction(UPDATE_CLICKED);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        remoteViews.setOnClickPendingIntent(R.id.widget_weather_layout, pendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.widget_update_icon, PendingIntent.getBroadcast(context, 0, startSyncIntent, 0));

        remoteViews.setOnClickPendingIntent(R.id.widget_weather_layout, pendingIntent);

        remoteViews.setTextViewText(R.id.widget_date, "Cork");
        remoteViews.setTextViewText(R.id.widget_weather_description, weatherData.getSummary());
        remoteViews.setTextViewText(R.id.widget_temperature, WetWeatherUtils.formatTemperature(context ,weatherData.getTemperature()));
        remoteViews.setImageViewResource(R.id.widget_weather_icon, WetWeatherUtils.getResourceIdForWeatherCondition(weatherData.getIcon()));
        remoteViews.setTextViewText(R.id.widget_updated_at, WetWeatherUtils.getUpdateTime(context, weatherData.getDateTimeMillis()));

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
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

