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

public class WPClimaCell {

    private static final String LOG_TAG = WPClimaCell.class.getName();
    //ClimaCell key. replace DEMO with actual api key.
    private static final String ClimaCellKey = "Demo";

    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "units";

    /**
     * Grab weather data from API
     *
     * @param context Android context
     * @return List of WeatherItems
     * @throws IOException Related to network and stream reading
     */
    public static List<WeatherItem> fetchWeatherData(Context context) throws IOException {
        String weatherType = "timelines";
        String queryParameterFields = "temperature,temperatureMax,temperatureMin,temperatureApparent,temperatureApparentMax,temperatureApparentMin,cloudCover,humidity,moonPhase,precipitationIntensity,precipitationProbability,precipitationType,pressureSurfaceLevel,visibility,weatherCode,windDirection,windGust,windSpeed,dewPoint,sunriseTime,sunsetTime,uvIndex";
        URL requestUrl = buildClimaCellUrlWithLatitudeLongitude(context, weatherType, queryParameterFields);
        if (requestUrl == null) {
            return null;
        }
        String response = NetworkUtils.getResponseFromHttpUrl(requestUrl);

        List<WeatherItem> weatherListArray = extractTomorrowWeather(context, response);
        WetWeatherUtils.updateCurrentlySunriseSunset(weatherListArray);

        return weatherListArray;
    }

    /**
     * Extract weather data from json
     * @param context Android Context to access preferences and resources
     * @param jsonResponse json String
     * @return list of weather data objects
     */
    private static List<WeatherItem> extractTomorrowWeather(Context context, String jsonResponse) {
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
            JSONObject data = root.getJSONObject("data");
            JSONArray timelines = data.getJSONArray("timelines");

            JSONObject current = timelines.getJSONObject(0);
            JSONArray intervals = current.getJSONArray("intervals");
            JSONObject currently = intervals.getJSONObject(0);
            JSONObject currentWeather = currently.getJSONObject("values");
            weatherListArray.add(extractSingleItem(context, currentWeather, currently.optString("startTime"), 1));

            JSONObject daily = timelines.getJSONObject(1);
            JSONArray dailyIntervals = daily.getJSONArray("intervals");
            for (int i = 0; i < dailyIntervals.length(); i++) {
                JSONObject dailyItem = dailyIntervals.getJSONObject(i);
                JSONObject dailyWeather = dailyItem.getJSONObject("values");
                weatherListArray.add(extractSingleItem(context, dailyWeather, dailyItem.optString("startTime"), 3));
            }

            JSONObject hourly = timelines.getJSONObject(2);
            JSONArray hourlyIntervals = hourly.getJSONArray("intervals");
            for (int i = 0; i < hourlyIntervals.length(); i++) {
                JSONObject hourlyItem = hourlyIntervals.getJSONObject(i);
                JSONObject hourlyWeather = hourlyItem.getJSONObject("values");
                weatherListArray.add(extractSingleItem(context, hourlyWeather, hourlyItem.optString("startTime"), 2));
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
    private static WeatherItem extractSingleItem(Context context, JSONObject item, String t, int weatherType) {
        long time = WetWeatherUtils.convertTime(t);
        if (weatherType == 3){
            time = time - 21600; // -6 hours as for daily forecast server returns time 6:00
        }
        String summary;
        String icon;
        double pressure;
        double humidity;
        String precipIntensity;
        String precipProbability;
        String precipType;
        long sunriseTime;
        long sunsetTime;
        String windSpeed;
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

        summary = WetWeatherUtils.getSummaryForClimaCell(context, item.optString("weatherCode"));
        icon = item.optString("weatherCode");
        pressure = item.optDouble("pressureSurfaceLevel");
        humidity = item.optDouble("humidity")/100;
        precipIntensity = item.optString("precipitationIntensity");
        precipProbability = preparePercentageData(item.optString("precipitationProbability"));
        precipType = item.optString("precipitationType");
        sunriseTime = WetWeatherUtils.convertTime(item.optString("sunriseTime"));
        sunsetTime = WetWeatherUtils.convertTime(item.optString("sunsetTime"));
        windSpeed = item.optString("windSpeed");
        windGust = item.optString("windGust");
        windDirection = item.optInt("windDirection");
        moonPhase = item.optString("moonPhase");
        dewPoint = item.optString("dewPoint");
        cloudCover = item.optDouble("cloudCover")/100;
        uvIndex = item.optString("uvIndex");
        visibility = item.optString("visibility");
        ozone = item.optString("ozone");
        temperatureHigh = item.optString("temperatureMax");
        temperatureLow = item.optString("temperatureMin");
        apparentTemperatureHigh = item.optString("temperatureApparentMax");
        apparentTemperatureLow = item.optString("temperatureApparentMin");
        apparentTemperature = item.optString("temperatureApparent");
        temperature = item.optString("temperature");

        return new WeatherItem(weatherType, time, summary, icon, pressure, humidity, precipIntensity,
                precipProbability, precipType, sunriseTime, sunsetTime, windSpeed, windGust,
                windDirection, moonPhase, dewPoint, cloudCover, uvIndex, visibility, ozone,
                temperatureHigh, temperatureLow, apparentTemperatureHigh,
                apparentTemperatureLow, apparentTemperature, temperature);

    }

    /**
     * Convert value 45 to 0.45 for to support common behavior with other data providers
     * @param data String value to convert
     * @return converted value
     */
    private static String preparePercentageData(String data){
        if (data == null || data.equals("")){
            return null;
        }
        return String.valueOf(Double.parseDouble(data)/100);
    }

    /**
     * Builds the URL used to talk to the ClimaCell weather server using latitude and longitude of a
     * location to get Realtime weather.
     *
     * @param context Android context to access Shared Preferences
     * @return The Url to use to query the weather server.
     */
    private static URL buildClimaCellUrlWithLatitudeLongitude(Context context, String weatherType,
                                                              String queryParameterFields) {

        String forecastBaseUrl = "https://api.tomorrow.io/v4";
        String queryParameterUnits = WetWeatherPreferences.getPreferencesUnits(context);
        String coordinates = WetWeatherPreferences.getPreferencesLatitude(context) + "," +
                WetWeatherPreferences.getPreferencesLongitude(context);
        Uri weatherQueryUri = Uri.parse(forecastBaseUrl).buildUpon()
                .appendEncodedPath(weatherType)
                .appendQueryParameter("location", coordinates)
                .appendQueryParameter(UNITS_PARAM, queryParameterUnits)
                .appendQueryParameter("fields", queryParameterFields)
                .appendQueryParameter("timesteps", "current,1d,1h")
                .appendQueryParameter("apikey", ClimaCellKey)
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
}
