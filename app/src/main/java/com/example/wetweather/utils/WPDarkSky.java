package com.example.wetweather.utils;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.wetweather.R;
import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.WetWeatherPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WPDarkSky {

    private static final String LOG_TAG = WPDarkSky.class.getName();
    //Dark Sky key. replace DEMO with actual api key.
    private static final String darkSkyKey = "Demo";

    /* The language parameter allows us to choose language for response from our API */
    private static final String LANG_PARAM = "lang";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    public static List<WeatherItem> fetchWeatherData(Context context) throws IOException {

        URL requestUrl = buildDarkSkyUrlWithLatitudeLongitude(context);
        if (requestUrl == null) {
            return null;
        }

        String response = NetworkUtils.getResponseFromHttpUrl(requestUrl);

        List<WeatherItem> weatherListArray = extractDarkSkyJSONResponse(response);

        updateCurrentlySunriseSunset(weatherListArray);

        return weatherListArray;
    }

    /**
     * Helper method to update currently object with Sunrise and Sunset time
     *
     * @param weatherListArray list of WeatherItem objects to work with
     */
    private static void updateCurrentlySunriseSunset(List<WeatherItem> weatherListArray) {

        if (weatherListArray.size() < 2) {
            return;
        }

        WeatherItem currently = null;
        for (WeatherItem item : weatherListArray) {
            if (item.weatherType == 1) {
                currently = item;
                break;
            }
        }

        if (currently == null) {
            return;
        }

        for (WeatherItem item : weatherListArray) {
            if (item.weatherType == 3 & DateUtils.isToday((item.getDateTimeInSeconds()) * 1000L)) {
                currently.setSunriseTime(item.getSunriseTime());
                currently.setSunsetTime(item.getSunsetTime());
                currently.moonPhase = item.moonPhase;
                break;
            }
        }

    }

    /**
     * Helper method to parse jsonResponse from Dark Sky
     *
     * @param jsonResponse Whole response from server as Strings
     * @return an {@link WeatherItem} array that has been built up from parsing a JSON response.
     */
    private static List<WeatherItem> extractDarkSkyJSONResponse(String jsonResponse) {
        /*
        weather types:
        1 - currently
        2 - hourly
        3 - daily
        4 - alerts array
        5 - alerts summary
        6 - info daily
        7 - info hourly
        8 - info minutely
         */

        List<WeatherItem> weatherListArray = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(jsonResponse);

            if (root.has("alerts")) {
                JSONArray alerts = root.getJSONArray("alerts");
                weatherListArray.add(extractAlertSummary(alerts, 5));
                for (int i = 0; i < alerts.length(); i++) {
                    JSONObject item = alerts.getJSONObject(i);
                    weatherListArray.add(extractAlerts(item, 4));
                }
            }

            if (root.has("currently")) {
                JSONObject currently = root.getJSONObject("currently");
                weatherListArray.add(extractSingleItem(currently, 1));
            }

            if (root.has("minutely")) {
                JSONObject minutelyInfo = root.getJSONObject("minutely");
                weatherListArray.add(extractInfo(minutelyInfo, 8));
            }

            if (root.has("hourly")) {
                JSONObject hourlyInfo = root.getJSONObject("hourly");
                weatherListArray.add(extractInfo(hourlyInfo, 7));

                JSONArray hourlyWeatherArray = hourlyInfo.getJSONArray("data");
                for (int i = 0; i < hourlyWeatherArray.length(); i++) {
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

    /**
     * This method extracts data from single JSONObject
     *
     * @param item        JSONObject to extract data from
     * @param weatherType value to determine type of the weather data
     * @return an {@link WeatherItem} object that has been built up from extracted data
     */
    private static WeatherItem extractSingleItem(JSONObject item, int weatherType) {
        long time;
        String summary;
        String icon;
        double pressure;
        double humidity;
        String precipIntensity;
        String precipProbability;
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
        String visibility;
        String ozone;
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
        precipIntensity = item.optString("precipIntensity");
        precipProbability = item.optString("precipProbability");
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
        visibility = item.optString("visibility");
        ozone = item.optString("ozone");
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

    /**
     * This method extracts data from JSON info section
     *
     * @param item        JSONObject to extract info from
     * @param weatherType value to determine type of the weather data
     * @return an {@link WeatherItem} object that has been built up from extracted data
     */
    private static WeatherItem extractInfo(JSONObject item, int weatherType) {
        String summary;
        String icon;

        summary = item.optString("summary");
        icon = item.optString("icon");

        return new WeatherItem(weatherType, summary, icon);
    }

    /**
     * This method extracts alert summary from JSONArray alerts
     *
     * @param alerts      JSONArray to extract info from
     * @param weatherType value to determine type of the weather data
     * @return an {@link WeatherItem} object that has been built up from extracted data
     * @throws JSONException in case extractig fails
     */
    private static WeatherItem extractAlertSummary(JSONArray alerts, int weatherType) throws JSONException {

        int count = alerts.length();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            JSONObject item = alerts.getJSONObject(i);
            sb.append(item.optString("title"));
            sb.append("; ");
        }
        return new WeatherItem(weatherType, sb.toString(), String.valueOf(count));
    }

    /**
     * This method extracts data from JSON alerts section
     *
     * @param item        JSONObject to extract info from
     * @param weatherType value to determine type of the weather data
     * @return an {@link WeatherItem} object that has been built up from extracted data
     */
    private static WeatherItem extractAlerts(JSONObject item, int weatherType) {
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
     * Builds the URL used to talk to the Dark Sky weather server using latitude and longitude of a
     * location.
     *
     * @param context Android context to access Shared Preferences
     * @return The Url to use to query the weather server.
     */
    private static URL buildDarkSkyUrlWithLatitudeLongitude(Context context) {

        String forecastBaseUrl = "https://api.darksky.net/forecast";

        String coordinates = WetWeatherPreferences.getPreferencesLatitude(context) + "," +
                WetWeatherPreferences.getPreferencesLongitude(context);

        String unitsFromPreferences = WetWeatherPreferences.getPreferencesUnits(context);
        String queryParameterUnits;
        if (unitsFromPreferences.equals(context.getString(R.string.pref_units_imperial))) {
            queryParameterUnits = "us";
        } else {
            queryParameterUnits = "si";
        }

        Uri weatherQueryUri = Uri.parse(forecastBaseUrl).buildUpon()
                .appendPath(darkSkyKey)
                .appendPath(coordinates)
                .appendQueryParameter(LANG_PARAM, WetWeatherPreferences.getPreferencesLanguage(context))
                .appendQueryParameter(UNITS_PARAM, queryParameterUnits)
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
}
