package com.example.wetweather.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

public final class WPOpenWeather {

    private static final String LOG_TAG = WPOpenWeather.class.getName();

    //OpenWeather key. replace DEMO with actual api key.
    private static final String openWeatherKey = "Demo";

    /* The language parameter allows us to choose language for response from our API */
    private static final String LANG_PARAM = "lang";
    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    public static List<WeatherItem> fetchWeatherData(Context context) throws IOException {

        URL requestUrl = buildOpenWeatherUrlWithLatitudeLongitude(context);
        if (requestUrl == null) {
            return null;
        }

        String response = NetworkUtils.getResponseFromHttpUrl(requestUrl);
        return extractOpenWeatherJSONResponse(response);
    }

    /**
     * Helper method to parse jsonResponse from Open Weather API
     *
     * @param jsonResponse Whole response from server as Strings
     * @return an {@link WeatherItem} array that has been built up from parsing a JSON response.
     */
    private static List<WeatherItem> extractOpenWeatherJSONResponse(String jsonResponse) {
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

            if (root.has("current")) {
                JSONObject currently = root.getJSONObject("current");
                weatherListArray.add(extractSingleItem(currently, 1));
            }

            if (root.has("hourly")) {

                JSONArray hourlyWeatherArray = root.getJSONArray("hourly");
                weatherListArray.add(extractInfo(hourlyWeatherArray.getJSONObject(0), 7));
                for (int i = 0; i < hourlyWeatherArray.length(); i++) {
                    JSONObject item = hourlyWeatherArray.getJSONObject(i);
                    weatherListArray.add(extractSingleItem(item, 2));
                }
            }

            if (root.has("daily")) {
                JSONArray dailyWeatherArray = root.getJSONArray("daily");
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

        try {
            if (item.has("weather")) {
                JSONArray weather = item.getJSONArray("weather");
                JSONObject witem = weather.getJSONObject(0);
                icon = witem.optString("icon");
                summary = witem.optString("description");
            } else {
                icon = "";
                summary = "";
            }
        } catch (JSONException e) {
            icon = "";
            summary = "";
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }

        time = item.optLong("dt");

        if (weatherType == 3) {
            time = time - 46800; //adjust time as for daily forecast server returns midday time.
            try {
                if (item.has("temp")) {
                    JSONObject temp = item.getJSONObject("temp");
                    temperatureHigh = temp.optString("max");
                    temperatureLow = temp.optString("min");
                } else {
                    temperatureHigh = "0";
                    temperatureLow = "0";
                }
            } catch (JSONException e) {
                temperatureHigh = "0";
                temperatureLow = "0";
                Log.e(LOG_TAG, "Problem parsing JSON results", e);
            }

            try {
                if (item.has("feels_like")) {
                    JSONObject temp = item.getJSONObject("feels_like");
                    apparentTemperatureHigh = temp.optString("day");
                    apparentTemperatureLow = temp.optString("night");
                } else {
                    apparentTemperatureHigh = "0";
                    apparentTemperatureLow = "0";
                }
            } catch (JSONException e) {
                apparentTemperatureHigh = "0";
                apparentTemperatureLow = "0";
                Log.e(LOG_TAG, "Problem parsing JSON results", e);
            }
        } else {
            temperatureHigh = "0";
            temperatureLow = "0";
            apparentTemperatureHigh = "0";
            apparentTemperatureLow = "0";
        }


        try {
            if (item.has("rain")) {
                String rainRawData = item.optString("rain");
                if (rainRawData.contains("{")) {
                    JSONObject rainObj = new JSONObject(rainRawData);
                    if (rainObj.has("1h")) {
                        precipIntensity = rainObj.optString("1h");
                        Log.i(LOG_TAG, "1h " + precipIntensity);
                    } else if (rainObj.has("3h")) {
                        precipIntensity = rainObj.optString("3h");
                        Log.i(LOG_TAG, "3h " + precipIntensity);
                    } else {
                        precipIntensity = "0.0";
                    }
                } else {
                    precipIntensity = rainRawData;
                    Log.i(LOG_TAG, "just rain " + precipIntensity);
                }
            } else {
                precipIntensity = "0.0";
            }
        } catch (JSONException e) {
            precipIntensity = "0.0";
        }

        precipProbability = "0";
        precipType = item.optString("precipType");

        pressure = item.optDouble("pressure");
        humidity = item.optDouble("humidity") / 100;
        sunriseTime = item.optLong("sunrise");
        sunsetTime = item.optLong("sunset");
        windSpeed = item.optDouble("wind_speed");
        windGust = item.optString("wind_gust");
        windDirection = item.optInt("wind_deg");
        moonPhase = item.optString("moonPhase");
        dewPoint = item.optString("dew_point");
        cloudCover = item.optDouble("clouds") / 100;
        uvIndex = item.optString("uvi");
        visibility = item.optString("visibility");
        ozone = item.optString("ozone");
        apparentTemperature = item.optString("feels_like");
        temperature = item.optString("temp");

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

        try {
            if (item.has("weather")) {
                JSONArray weather = item.getJSONArray("weather");
                JSONObject witem = weather.getJSONObject(0);
                icon = witem.optString("icon");
                summary = witem.optString("description");
            } else {
                icon = "";
                summary = "";
            }
        } catch (JSONException e) {
            icon = "";
            summary = "";
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }

        return new WeatherItem(weatherType, summary, icon);
    }

    /**
     * This method extracts alert summary from JSONArray alerts
     *
     * @param alerts      JSONArray to extract info from
     * @param weatherType value to determine type of the weather data
     * @return an {@link WeatherItem} object that has been built up from extracted data
     * @throws JSONException in case extracting fails
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
     * Builds the URL used to talk to the OpenWeather server using latitude and longitude of a
     * location.
     *
     * @param context Android context to access Shared Preferences
     * @return The Url to use to query the weather server.
     */
    private static URL buildOpenWeatherUrlWithLatitudeLongitude(Context context) {
        String forecastBaseUrl = "https://api.openweathermap.org/data/2.5/onecall";
        Uri weatherQueryUri = Uri.parse(forecastBaseUrl).buildUpon()
                .appendQueryParameter("lat", WetWeatherPreferences.getPreferencesLatitude(context))
                .appendQueryParameter("lon", WetWeatherPreferences.getPreferencesLongitude(context))
                .appendQueryParameter("appid", openWeatherKey)
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

    private static WeatherItem createEmptyInfo(int weatherType) {
        String summary = "";
        ;
        String icon = "";
        ;

        return new WeatherItem(weatherType, summary, icon);
    }

}
