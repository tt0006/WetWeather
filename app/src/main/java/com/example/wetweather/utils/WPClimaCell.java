package com.example.wetweather.utils;

import android.content.Context;
import android.net.Uri;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WPClimaCell {

    private static final String LOG_TAG = WPClimaCell.class.getName();
    //ClimaCell key. replace DEMO with actual api key.
    private static final String ClimaCellKey = "Demo";

    /* The units parameter allows us to designate whether we want metric units or imperial units */
    private static final String UNITS_PARAM = "unit_system";

    /**
     * Grab weather data from API
     *
     * @param context Android context
     * @return List of WeatherItems
     * @throws IOException Related to network and stream reading
     */
    public static List<WeatherItem> fetchWeatherData(Context context) throws IOException {
        List<WeatherItem> weatherListArray = new ArrayList<>();

        // Extract current weather
        String realTimeType = "realtime";
        String realTimeQueryParameterFields = "temp,feels_like,humidity,wind_speed,wind_direction,wind_gust,baro_pressure,precipitation,precipitation_type,sunrise,sunset,visibility,cloud_cover,moon_phase,weather_code";
        URL realTimeRequestUrl = buildClimaCellUrlWithLatitudeLongitude(context, realTimeType, realTimeQueryParameterFields);
        if (realTimeRequestUrl == null) {
            return null;
        }
        String realTimeResponse = NetworkUtils.getResponseFromHttpUrl(realTimeRequestUrl);
        try {
            JSONObject root = new JSONObject(realTimeResponse);
            weatherListArray.add(extractRealTimeHourlyJSONResponse(root, 1));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }

        // Extract hourly weather
        String hourlyType = "forecast/hourly";
        String hourlyQueryParameterFields = "temp,feels_like,humidity,wind_speed,wind_direction,wind_gust,baro_pressure,precipitation,precipitation_type,sunrise,sunset,visibility,cloud_cover,moon_phase,weather_code";
        URL hourlyRequestUrl = buildClimaCellUrlWithLatitudeLongitude(context, hourlyType, hourlyQueryParameterFields);
        if (hourlyRequestUrl == null) {
            return null;
        }
        String hourlyResponse = NetworkUtils.getResponseFromHttpUrl(hourlyRequestUrl);
        try {
            JSONArray hourlyWeatherArray = new JSONArray(hourlyResponse);
            for (int i = 0; i < hourlyWeatherArray.length(); i++) {
                JSONObject item = hourlyWeatherArray.getJSONObject(i);
                weatherListArray.add(extractRealTimeHourlyJSONResponse(item, 2));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }

        //extract daily weather
        String dailyType = "forecast/daily";
        String dailyQueryParameterFields = "temp,feels_like,humidity,wind_speed,wind_direction,baro_pressure,precipitation,precipitation_probability,sunrise,sunset,visibility,weather_code";
        URL dailyRequestUrl = buildClimaCellUrlWithLatitudeLongitude(context, dailyType, dailyQueryParameterFields);
        if (dailyRequestUrl == null) {
            return null;
        }
        String dailyResponse = NetworkUtils.getResponseFromHttpUrl(dailyRequestUrl);
        try {
            JSONArray dailyWeatherArray = new JSONArray(dailyResponse);
            for (int i = 0; i < dailyWeatherArray.length(); i++) {
                JSONObject item = dailyWeatherArray.getJSONObject(i);
                weatherListArray.add(extractDailyJSONResponse(item, 3));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
        }


        return weatherListArray;
    }

    private static WeatherItem extractDailyJSONResponse(JSONObject item, int weatherType) {
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

        // no values here
        temperature = "0";
        precipType = "";
        ozone = "0";
        apparentTemperature = "0";
        cloudCover = 0.0;
        moonPhase = "0";
        dewPoint = "0";
        uvIndex = "0";

        try {
            if (item.has("temp")) {
                JSONArray temp = item.getJSONArray("temp");
                JSONObject lowTemp = temp.getJSONObject(0).getJSONObject("min");
                temperatureLow = lowTemp.optString("value");

                JSONObject highTemp = temp.getJSONObject(1).getJSONObject("max");
                temperatureHigh = highTemp.optString("value");
            } else {
                temperatureLow = "0";
                temperatureHigh = "0";
            }

            if (item.has("feels_like")) {
                JSONArray temp = item.getJSONArray("feels_like");
                JSONObject lowTemp = temp.getJSONObject(0).getJSONObject("min");
                apparentTemperatureLow = lowTemp.optString("value");

                JSONObject highTemp = temp.getJSONObject(1).getJSONObject("max");
                apparentTemperatureHigh = highTemp.optString("value");
            } else {
                apparentTemperatureLow = "0";
                apparentTemperatureHigh = "0";
            }

            if (item.has("wind_speed")) {
                JSONArray temp = item.getJSONArray("wind_speed");
                JSONObject min = temp.getJSONObject(0).getJSONObject("min");
                windSpeed = min.optString("value");

                JSONObject max = temp.getJSONObject(1).getJSONObject("max");
                windGust = max.optString("value");
            } else {
                windSpeed = "";
                windGust = "";
            }

            if (item.has("baro_pressure")) {
                JSONArray temp = item.getJSONArray("baro_pressure");
                JSONObject max = temp.getJSONObject(1).getJSONObject("max");
                pressure = max.optDouble("value");
                Log.i(LOG_TAG, "pressure = "+pressure);
            } else {
                pressure = 0.0;
            }

            if (item.has("visibility")) {
                JSONArray temp = item.getJSONArray("visibility");
                JSONObject max = temp.getJSONObject(1).getJSONObject("max");
                visibility = max.optString("value");
            } else {
                visibility = "0";
            }

            if (item.has("humidity")) {
                JSONArray temp = item.getJSONArray("humidity");
                JSONObject max = temp.getJSONObject(1).getJSONObject("max");
                humidity = max.optDouble("value");
            } else {
                humidity = 0.0;
            }

            if (item.has("wind_direction")) {
                JSONArray temp = item.getJSONArray("wind_direction");
                JSONObject max = temp.getJSONObject(1).getJSONObject("max");
                windDirection = max.optInt("value");
            } else {
                windDirection = 0;
            }

            if (item.has("precipitation")) {
                JSONArray temp = item.getJSONArray("precipitation");
                JSONObject max = temp.getJSONObject(0).getJSONObject("max");
                precipIntensity = max.optString("value");
            } else {
                precipIntensity = "0";
            }

            if (item.has("precipitation_probability")) {
                JSONObject precipitation_probability = item.getJSONObject("precipitation_probability");
                precipProbability = precipitation_probability.optString("value");
            } else {
                precipProbability = "-1";
            }

            if (item.has("sunrise")) {
                JSONObject sunrise = item.getJSONObject("sunrise");
                String riseTime = sunrise.optString("value");
                Instant instant = Instant.parse(riseTime);
                sunriseTime = instant.toEpochMilli();
            } else {
                sunriseTime = 0L;
            }

            if (item.has("sunset")) {
                JSONObject sunset = item.getJSONObject("sunset");
                String setTime = sunset.optString("value");
                Instant instant = Instant.parse(setTime);
                sunsetTime = instant.toEpochMilli();
            } else {
                sunsetTime = 0L;
            }

            if (item.has("observation_time")) {
                JSONObject observation_time = item.getJSONObject("observation_time");
                String obsTime = observation_time.optString("value");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(obsTime);
                assert date != null;
                time = date.getTime();
            } else {
                time = 0L;
            }

            if (item.has("weather_code")) {
                /*
                freezing_rain_heavy, freezing_rain, freezing_rain_light, freezing_drizzle,
                 ice_pellets_heavy, ice_pellets, ice_pellets_light, snow_heavy, snow, snow_light,
                 flurries, tstorm, rain_heavy, rain, rain_light, drizzle, fog_light, fog, cloudy,
                 mostly_cloudy, partly_cloudy, mostly_clear, clear
                 */
                JSONObject weather_code = item.getJSONObject("weather_code");
                icon = weather_code.optString("value");
            } else {
                icon = "0";
            }


        } catch (JSONException | ParseException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
            temperatureLow = "0";
            temperatureHigh = "0";
            apparentTemperatureHigh = "0";
            apparentTemperatureLow = "0";

            windSpeed = "";
            windGust = "";
            pressure = 0.0;
            visibility = "0";
            humidity = 0.0;
            windDirection = 0;
            precipIntensity = "0";
            sunriseTime = 0L;
            sunsetTime = 0L;
            time = 0L;
            icon = "0";
            precipProbability = "";
        }

        summary = icon;


        return new WeatherItem(weatherType, time, summary, icon, pressure, humidity, precipIntensity,
                precipProbability, precipType, sunriseTime, sunsetTime, windSpeed, windGust,
                windDirection, moonPhase, dewPoint, cloudCover, uvIndex, visibility, ozone,
                temperatureHigh, temperatureLow, apparentTemperatureHigh,
                apparentTemperatureLow, apparentTemperature, temperature);

    }

    /**
     * This method extracts data from single JSONObject for Realtime and Hourly data
     *
     * @param item String to extract data from
     * @return an {@link WeatherItem} object that has been built up from extracted data
     */
    private static WeatherItem extractRealTimeHourlyJSONResponse(JSONObject item, int weatherType) {
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

        try {
            if (item.has("temp")) {
                JSONObject temp = item.getJSONObject("temp");
                temperature = temp.optString("value");
            } else {
                temperature = "0";
            }

            if (item.has("feels_like")) {
                JSONObject feels_like = item.getJSONObject("feels_like");
                apparentTemperature = feels_like.optString("value");
            } else {
                apparentTemperature = "0";
            }

            if (item.has("wind_speed")) {
                JSONObject wind_speed = item.getJSONObject("wind_speed");
                windSpeed = wind_speed.optString("value");
            } else {
                windSpeed = "";
            }

            if (item.has("wind_gust")) {
                JSONObject wind_gust = item.getJSONObject("wind_gust");
                windGust = wind_gust.optString("value");
            } else {
                windGust = "";
            }

            if (item.has("baro_pressure")) {
                JSONObject baro_pressure = item.getJSONObject("baro_pressure");
                pressure = baro_pressure.optDouble("value");
                Log.i(LOG_TAG, "pressure = "+pressure);
            } else {
                pressure = 0.0;
            }

            if (item.has("visibility")) {
                JSONObject visibilityObj = item.getJSONObject("visibility");
                visibility = visibilityObj.optString("value");
            } else {
                visibility = "0";
            }

            if (item.has("humidity")) {
                JSONObject humidityObj = item.getJSONObject("humidity");
                humidity = humidityObj.optDouble("value");
            } else {
                humidity = 0.0;
            }

            if (item.has("wind_direction")) {
                JSONObject wind_direction = item.getJSONObject("wind_direction");
                windDirection = wind_direction.optInt("value");
            } else {
                windDirection = 0;
            }

            if (item.has("precipitation")) {
                JSONObject precipitation = item.getJSONObject("precipitation");
                precipIntensity = precipitation.optString("value");
            } else {
                precipIntensity = "0";
            }

            if (item.has("precipitation_type")) {
                JSONObject precipitation_type = item.getJSONObject("precipitation_type");
                precipType = precipitation_type.optString("value");
            } else {
                precipType = "0";
            }

            if (item.has("cloud_cover")) {
                JSONObject cloud_cover = item.getJSONObject("cloud_cover");
                cloudCover = cloud_cover.optDouble("value");
            } else {
                cloudCover = 0.0;
            }

            if (item.has("sunrise")) {
                JSONObject sunrise = item.getJSONObject("sunrise");
                String riseTime = sunrise.optString("value");
                Instant instant = Instant.parse(riseTime);
                sunriseTime = instant.toEpochMilli();
            } else {
                sunriseTime = 0L;
            }

            if (item.has("sunset")) {
                JSONObject sunset = item.getJSONObject("sunset");
                String setTime = sunset.optString("value");
                Instant instant = Instant.parse(setTime);
                sunsetTime = instant.toEpochMilli();
            } else {
                sunsetTime = 0L;
            }

            if (item.has("observation_time")) {
                JSONObject observation_time = item.getJSONObject("observation_time");
                String obsTime = observation_time.optString("value");
                Instant instant = Instant.parse(obsTime);
                time = instant.toEpochMilli();
            } else {
                time = 0L;
            }

            if (item.has("moon_phase")) {
                JSONObject moon_phase = item.getJSONObject("moon_phase");
                moonPhase = moon_phase.optString("value");
            } else {
                moonPhase = "0";
            }

            if (item.has("weather_code")) {
                /*
                freezing_rain_heavy, freezing_rain, freezing_rain_light, freezing_drizzle,
                 ice_pellets_heavy, ice_pellets, ice_pellets_light, snow_heavy, snow, snow_light,
                 flurries, tstorm, rain_heavy, rain, rain_light, drizzle, fog_light, fog, cloudy,
                 mostly_cloudy, partly_cloudy, mostly_clear, clear
                 */
                JSONObject weather_code = item.getJSONObject("weather_code");
                icon = weather_code.optString("value");
            } else {
                icon = "0";
            }


            if (item.has("precipitation_probability")) {
                JSONObject precipitation_probability = item.getJSONObject("precipitation_probability");
                precipProbability = precipitation_probability.optString("value");
            } else {
                precipProbability = "0";
            }

            if (item.has("dewpoint")) {
                JSONObject dewpoint = item.getJSONObject("dewpoint");
                dewPoint = dewpoint.optString("value");
            } else {
                dewPoint = "0";
            }

            if (item.has("surface_shortwave_radiation")) {
                JSONObject surface_shortwave_radiation = item.getJSONObject("surface_shortwave_radiation");
                uvIndex = surface_shortwave_radiation.optString("value");
            } else {
                uvIndex = "0";
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON results", e);
            temperature = "0";
            apparentTemperature = "0";
            windSpeed = "";
            windGust = "";
            pressure = 0.0;
            visibility = "0";
            humidity = 0.0;
            windDirection = 0;
            precipIntensity = "0";
            precipType = "0";
            cloudCover = 0.0;
            sunriseTime = 0L;
            sunsetTime = 0L;
            time = 0L;
            moonPhase = "0";
            icon = "0";
            precipProbability = "0";
            dewPoint = "0";
            uvIndex = "0";
        }

        summary = icon;
        ozone = "0";

        temperatureHigh = "0";
        temperatureLow = "0";
        apparentTemperatureHigh = "0";
        apparentTemperatureLow = "0";

        return new WeatherItem(weatherType, time, summary, icon, pressure, humidity, precipIntensity,
                precipProbability, precipType, sunriseTime, sunsetTime, windSpeed, windGust,
                windDirection, moonPhase, dewPoint, cloudCover, uvIndex, visibility, ozone,
                temperatureHigh, temperatureLow, apparentTemperatureHigh,
                apparentTemperatureLow, apparentTemperature, temperature);

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

        String forecastBaseUrl = "https://api.climacell.co/v3/weather";
        String unitsFromPreferences = WetWeatherPreferences.getPreferencesUnits(context);
        String queryParameterUnits;
        if (unitsFromPreferences.equals(context.getString(R.string.pref_units_imperial))) {
            queryParameterUnits = "us";
        } else {
            queryParameterUnits = "si";
        }

        Uri weatherQueryUri = Uri.parse(forecastBaseUrl).buildUpon()
                .appendEncodedPath(weatherType)
                .appendQueryParameter("lat", WetWeatherPreferences.getPreferencesLatitude(context))
                .appendQueryParameter("lon", WetWeatherPreferences.getPreferencesLongitude(context))
                .appendQueryParameter(UNITS_PARAM, queryParameterUnits)
                .appendQueryParameter("fields", queryParameterFields)
                .appendQueryParameter("apikey", ClimaCellKey)
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
     * Builds the URL used to talk to the ClimaCell weather server using latitude and longitude of a
     * location to get Realtime weather.
     *
     * @param context Android context to access Shared Preferences
     * @return The Url to use to query the weather server.
     */
    private static URL buildForecastClimaCellUrlWithLatitudeLongitude(Context context, String weatherType,
                                                              String queryParameterFields) {

        String forecastBaseUrl = "https://api.climacell.co/v3/weather";
        String unitsFromPreferences = WetWeatherPreferences.getPreferencesUnits(context);
        String queryParameterUnits;
        if (unitsFromPreferences.equals(context.getString(R.string.pref_units_imperial))) {
            queryParameterUnits = "us";
        } else {
            queryParameterUnits = "si";
        }

        Uri weatherQueryUri = Uri.parse(forecastBaseUrl).buildUpon()
                .appendEncodedPath("")
                .appendPath(weatherType)
                .appendQueryParameter("lat", WetWeatherPreferences.getPreferencesLatitude(context))
                .appendQueryParameter("lon", WetWeatherPreferences.getPreferencesLongitude(context))
                .appendQueryParameter(UNITS_PARAM, queryParameterUnits)
                .appendQueryParameter("fields", queryParameterFields)
                .appendQueryParameter("apikey", ClimaCellKey)
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
