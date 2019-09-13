package com.example.wetweather.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.wetweather.R;

import java.text.DateFormat;
import java.util.Date;

public class WetWeatherUtils {
    private static final String LOG_TAG = WetWeatherUtils.class.getSimpleName();


    /**
     * format the temperature so that no decimal points show. Temperatures will be formatted
     * to the following form: "21°"
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees
     *
     * @return Formatted temperature String in the following form:
     * "21°"
     */
    public static String formatTemperature(Context context, String temperature) {
        int temperatureFormatResourceId = R.string.format_temperature;
        Double temp;
        try{
            temp = Double.parseDouble(temperature);
        } catch (NumberFormatException e){
            temp = 0.0;
        }

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temp);
    }

    public static String convertDate(Context context, long date){

        Date d = new Date(date*1000L);

        if (DateUtils.isToday(date*1000L)){
            return context.getString(R.string.today);
        }

        DateFormat df = DateFormat.getDateInstance();
        return df.format(d);
    }


    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
     *                  See https://www.mathsisfun.com/geometry/degrees.html
     *
     * @return Wind String in the following form: "2 km/h SW"
     */
    public static String getFormattedWind(Context context, double windSpeed, double degrees) {
        int windFormat = R.string.format_wind_ms;

        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }

        return String.format(context.getString(windFormat), windSpeed, direction);
    }


    /**
     * Helper method to provide the icon resource id according to the weather condition
     */
    public static int getSmallArtResourceIdForWeatherCondition(String weatherIcon) {

        switch (weatherIcon) {
            case "clear-day":
            case "clear-night":
                return R.drawable.ic_clear;
            case "rain":
                return R.drawable.ic_rain;
            case "snow":
            case "sleet":
                return R.drawable.ic_snow;
            case "fog":
                return R.drawable.ic_fog;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "partly-cloudy-day":
            case "partly-cloudy-night":
                return R.drawable.ic_light_clouds;
            default:
                return R.drawable.ic_clear;

        }
    }

    public static int getLargeArtResourceIdForWeatherCondition(String weatherIcon) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night

        switch (weatherIcon){
            case "clear-day":
            case "clear-night":
                return R.drawable.art_clear;
            case "rain":
                return R.drawable.art_rain;
            case "snow":
            case "sleet":
                return R.drawable.art_snow;
            case "fog":
                return R.drawable.art_fog;
            case "cloudy":
                return R.drawable.art_clouds;
            case "partly-cloudy-day":
            case "partly-cloudy-night":
                return R.drawable.art_light_clouds;
                default:
                    return R.drawable.art_clear;
        }
}
}
