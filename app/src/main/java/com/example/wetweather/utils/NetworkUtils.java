package com.example.wetweather.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.wetweather.WeatherRepository;
import com.example.wetweather.WeatherWidget;
import com.example.wetweather.prefs.WetWeatherPreferences;
import com.example.wetweather.db.WeatherItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getName();

    private static final String FORECAST_BASE_URL = "https://api.darksky.net/forecast";
    //KEY. replace DEMO with actual api key.
    private static final String key = "DEMO";
    /* The language parameter allows us to choose language for response from our API */
    private static final String LANG_PARAM = "lang";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    /**
     * Processing method which combines multiple actions:
     * make network request using getResponseFromHttpUrl method
     * parse response using extractJSONrequest method
     * insert new data to db using WeatherRepository class
     * Return true if success or false if not (used for scheduled updates worker class)
     */
    public static boolean updateWeatherData(Context context){

        String coordinates = WetWeatherPreferences.getPreferencesLatitudeLongitude(context);
        URL requestUrl = buildUrlWithLatitudeLongitude(context, coordinates);

        if (requestUrl == null){
            return false;
        }

        try {

            String jsonResponse = getResponseFromHttpUrl(requestUrl);

            List<WeatherItem> weatherListArray = extractJSONrequest(jsonResponse);

            WeatherRepository.getInstance(context).insertData(weatherListArray);

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
            return false;
        }
        updateAllWidgets(context);
        return true;
    }


    /**
     * Return an {@link WeatherItem} array that has been built up from
     * parsing a JSON response.
     */
    private static List<WeatherItem> extractJSONrequest(String jsonRequest) {
        // weather types:
        // 1 - currently
        // 2 - hourly
        // 3 - daily
        // 4 - alerts array
        // 5 - alerts summary
        // 6 - info daily
        // 7 - info hourly
        // 8 - info minutely

        List<WeatherItem> weatherListArray = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(jsonRequest);

            if (root.has("alerts")){
                JSONArray alerts = root.getJSONArray("alerts");
                weatherListArray.add(extractAlertSummary(alerts, 5));
                for (int i=0; i<alerts.length(); i++) {
                    JSONObject item = alerts.getJSONObject(i);
                    weatherListArray.add(extractAlerts(item, 4));
                }
            }

            if (root.has("currently")){
                JSONObject currently = root.getJSONObject("currently");
                weatherListArray.add(extractSingleItem(currently, 1));}

            if (root.has("minutely")){
                JSONObject minutelyInfo = root.getJSONObject("minutely");
                weatherListArray.add(extractInfo(minutelyInfo, 8));}

            if (root.has("hourly")){
                JSONObject hourlyInfo = root.getJSONObject("hourly");
                weatherListArray.add(extractInfo(hourlyInfo, 7));

                JSONArray hourlyWeatherArray = hourlyInfo.getJSONArray("data");
                for (int i=0; i<24; i++) {
                    JSONObject item = hourlyWeatherArray.getJSONObject(i);
                    weatherListArray.add(extractSingleItem(item, 2));
                }
            }

            if (root.has("daily")) {
                JSONObject dailyInfo = root.getJSONObject("daily");
                weatherListArray.add(extractInfo(dailyInfo, 6));

                JSONArray dailyWeatherArray = dailyInfo.getJSONArray("data");
                for (int i = 0; i < dailyWeatherArray.length(); i++) {
                    JSONObject item = dailyWeatherArray.getJSONObject(i);
                    weatherListArray.add(extractSingleItem(item, 3));
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }
        return weatherListArray;
    }

    private static WeatherItem extractSingleItem(JSONObject item, int weatherType){
        long time;
        String summary;
        String icon;
        double pressure;
        double humidity;
        double precipIntensity;
        double precipProbability;
        String precipType;
        long sunriseTime;
        long sunsetTime;
        double windSpeed;
        String windGust;
        int windDirection;
        String moonPhase;
        String dewPoint;
        double cloudCover;
        String uvIndex;
        double visibility;
        double ozone;
        String temperatureHigh;
        String temperatureLow;
        String apparentTemperatureHigh;
        String apparentTemperatureLow;
        String apparentTemperature;
        String temperature;

        time = item.optLong("time");
        summary = item.optString("summary");
        icon = item.optString("icon");
        pressure = item.optDouble("pressure");
        humidity = item.optDouble("humidity");
        precipIntensity = item.optDouble("precipIntensity");
        precipProbability = item.optDouble("precipProbability");
        precipType = item.optString("precipType");
        sunriseTime = item.optLong("sunriseTime");
        sunsetTime = item.optLong("sunsetTime");
        windSpeed = item.optDouble("windSpeed");
        windGust = item.optString("windGust");
        windDirection = item.optInt("windBearing");
        moonPhase = item.optString("moonPhase");
        dewPoint = item.optString("dewPoint");
        cloudCover = item.optDouble("cloudCover");
        uvIndex = item.optString("uvIndex");
        visibility = item.optDouble("visibility");
        ozone = item.optDouble("ozone");
        temperatureHigh = item.optString("temperatureHigh");
        temperatureLow = item.optString("temperatureLow");
        apparentTemperatureHigh = item.optString("apparentTemperatureHigh");
        apparentTemperatureLow = item.optString("apparentTemperatureLow");
        apparentTemperature = item.optString("apparentTemperature");
        temperature = item.optString("temperature");

        return new WeatherItem(weatherType, time, summary, icon, pressure, humidity, precipIntensity,
                precipProbability, precipType, sunriseTime, sunsetTime, windSpeed, windGust,
                windDirection, moonPhase, dewPoint, cloudCover, uvIndex, visibility, ozone,
                temperatureHigh, temperatureLow, apparentTemperatureHigh,
                apparentTemperatureLow, apparentTemperature, temperature);

    }

    private static WeatherItem extractInfo(JSONObject item, int weatherType){
        String summary;
        String icon;

        summary = item.optString("summary");
        icon = item.optString("icon");

        return new WeatherItem(weatherType, summary, icon);
    }

    private static WeatherItem extractAlertSummary(JSONArray alerts, int weatherType) throws JSONException {

        int count = alerts.length();

        StringBuilder sb = new StringBuilder();
        for (int i=0; i<count; i++){
            JSONObject item = alerts.getJSONObject(i);
            sb.append(item.optString("title"));
            sb.append("; ");
        }
        return new WeatherItem(weatherType, sb.toString(), String.valueOf(count));
    }

    private static WeatherItem extractAlerts(JSONObject item, int weatherType){
        String title;
        String severity;
        long time;
        long expires;
        String description;
        String uri;

        title = item.optString("title");
        severity = item.optString("severity");
        time = item.optLong("time");
        expires = item.optLong("expires");
        description = item.optString("description");
        uri = item.optString("uri");

        return new WeatherItem(weatherType, title, severity, time, expires, description, uri);
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param coordinates The latitude ad longitude of the location
     * @return The Url to use to query the weather server.
     */
    private static URL buildUrlWithLatitudeLongitude(Context context, String coordinates) {
        Uri weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath(coordinates)
                .appendQueryParameter(LANG_PARAM, WetWeatherPreferences.getPreferencesLanguage(context))
                .appendQueryParameter(UNITS_PARAM, WetWeatherPreferences.getPreferencesUnits(context))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.d(LOG_TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    //Helper method to update widgets
    private static void updateAllWidgets(Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
        if (appWidgetIds.length > 0) {
            new WeatherWidget().onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}
