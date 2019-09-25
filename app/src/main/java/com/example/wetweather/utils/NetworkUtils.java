package com.example.wetweather.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.wetweather.WetWeatherPreferences;
import com.example.wetweather.db.WeatherDB;
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
    //KEY
    private static final String key = "DEMO";
    /* The format parameter allows us to designate whether we want JSON or XML from our API */
    private static final String LANG_PARAM = "lang";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    /**
     * Processing method which combines multiple actios:
     * make network request using getResponseFromHttpUrl method
     * parse response using extractJSONrequest method
     * insert new data to db
     * Return true if success or false if not
     */
    public static boolean updateWeatherData(Context context){

        URL requestUrl = buildUrlWithLatitudeLongitude(context,51.896893,-8.486316);

        if (requestUrl == null){
            return false;
        }

        try {

            String jsonResponse = getResponseFromHttpUrl(requestUrl);

            List<WeatherItem> weatherListArray = extractJSONrequest(jsonResponse);

            WeatherDB db = WeatherDB.getInstance(context);
            if (weatherListArray.size()>0) {
                //delete all data in db
                db.weatherDao().deleteAll();
            } else
                return false;

            for (int i = 0; i < weatherListArray.size(); i++){
                db.weatherDao().insertWeatherItem(weatherListArray.get(i));
            }

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Return an {@link WeatherItem} array that has been built up from
     * parsing a JSON response.
     */
    private static List<WeatherItem> extractJSONrequest(String jsonRequest) {
        List<WeatherItem> weatherListArray = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(jsonRequest);


            JSONObject currently = root.getJSONObject("currently");
            weatherListArray.add(extractSingleItem(currently, 0));

            JSONObject minutelyInfo = root.getJSONObject("minutely");
            weatherListArray.add(extractInfo(minutelyInfo));

            JSONObject hourlyInfo = root.getJSONObject("hourly");
            weatherListArray.add(extractInfo(hourlyInfo));

            JSONArray hourlyWeatherArray = hourlyInfo.getJSONArray("data");
            for (int i=0; i< 24; i++) {
                JSONObject item = hourlyWeatherArray.getJSONObject(i);
                weatherListArray.add(extractSingleItem(item, 1));
            }

            JSONObject dailyInfo = root.getJSONObject("daily");
            weatherListArray.add(extractInfo(dailyInfo));

            JSONArray dailyWeatherArray = dailyInfo.getJSONArray("data");
            for (int i=0; i< dailyWeatherArray.length(); i++) {
                JSONObject item = dailyWeatherArray.getJSONObject(i);
                weatherListArray.add(extractSingleItem(item, 2));
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

    private static WeatherItem extractInfo(JSONObject item){
        String summary;
        String icon;

        summary = item.optString("summary");
        icon = item.optString("icon");

        return new WeatherItem(summary, icon);
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param latitude  The latitude of the location
     * @param longitude The longitude of the location
     * @return The Url to use to query the weather server.
     */
    private static URL buildUrlWithLatitudeLongitude(Context context, Double latitude, Double longitude) {
        Uri weatherQueryUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath(latitude+","+longitude)
                .appendQueryParameter(LANG_PARAM, WetWeatherPreferences.getPreferencesLanguage(context))
                .appendQueryParameter(UNITS_PARAM, WetWeatherPreferences.getPreferencesUnits(context))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.i(LOG_TAG, "URL: " + weatherQueryUrl);
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
}
