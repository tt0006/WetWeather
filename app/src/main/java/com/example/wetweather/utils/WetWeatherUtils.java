package com.example.wetweather.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.wetweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        if (null == temperature){
            return "";
        }
        int temperatureFormatResourceId = R.string.format_temperature;
        double temp;
        try{
            temp = Double.parseDouble(temperature);
        } catch (NumberFormatException e){
            temp = 0.0;
        }

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temp);
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
     * This method uses the wind direction in degrees to determine compass direction as icon.
     *
     * @param degrees   Degrees as measured on a compass
     *
     * @return Wind direction icon
     */
    public static int getWindIcon(double degrees) {
        int windIcon = R.drawable.ic_wind_icon;

        if (degrees >= 337.5 || degrees < 22.5) {
            windIcon = R.drawable.ic_wind_north;
        } else if (degrees >= 22.5 && degrees < 67.5) {
            windIcon = R.drawable.ic_wind_ne;
        } else if (degrees >= 67.5 && degrees < 112.5) {
            windIcon = R.drawable.ic_wind_east;
        } else if (degrees >= 112.5 && degrees < 157.5) {
            windIcon = R.drawable.ic_wind_se;
        } else if (degrees >= 157.5 && degrees < 202.5) {
            windIcon = R.drawable.ic_wind_south;
        } else if (degrees >= 202.5 && degrees < 247.5) {
            windIcon = R.drawable.ic_wind_sw;
        } else if (degrees >= 247.5 && degrees < 292.5) {
            windIcon = R.drawable.ic_wind_west;
        } else if (degrees >= 292.5 && degrees < 337.5) {
            windIcon = R.drawable.ic_wind_nw;
        }

        return windIcon;
    }


    /**
     * This method uses the weather description to determine its icon.
     *
     * @param weatherIcon weather description as per API documentation
     *
     * @return Weather icon
     */
    public static int getResourceIconIdForWeatherCondition(String weatherIcon) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night

        switch (weatherIcon){
            case "clear-day":
                return R.drawable.ic_clear_day_vector;
            case "clear-night":
                return R.drawable.ic_clear_night_vector;
            case "rain":
                return R.drawable.ic_rain_vector;
            case "snow":
                return R.drawable.ic_snow_vector;
            case "sleet":
                return R.drawable.ic_sleet_vector;
            case "fog":
                return R.drawable.ic_fog_vector;
            case "cloudy":
                return R.drawable.ic_cloudy_vector;
            case "partly-cloudy-day":
                return R.drawable.ic_partly_cloudy_day_vector;
            case "partly-cloudy-night":
                return R.drawable.ic_partly_cloudy_night_vector;
            case "wind":
                return R.drawable.ic_wind_vector;
            default:
                return R.drawable.art_clear;
        }
    }


    public static String getHourWithDay(Context context, long dateInSeconds){
        long dateInMillis = dateInSeconds*1000L;
        SimpleDateFormat dayFormat = new SimpleDateFormat("H:mm (E)");
        return dayFormat.format(dateInMillis);
    }


    public static String getDayName(Context context, long dateInSeconds) {
        /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
        long dateInMillis = dateInSeconds*1000L;

        if (DateUtils.isToday(dateInMillis)) {
            return context.getString(R.string.today);
        }else{
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, LLLL d");
            return dayFormat.format(dateInMillis);
        }
    }

    public static String getUpdateTime(Context context, long date){

        Date d = new Date(date*1000L);
        DateFormat df;

        if (DateUtils.isToday(date*1000L)){
            df = DateFormat.getTimeInstance();
        } else {
            df = DateFormat.getDateTimeInstance();
        }
        return String.format("%1$s %2$s",context.getString(R.string.updated_at_text), df.format(d));
    }

    public static String getTime(Context context, long date){
        if (date == 0L){
            return context.getString(R.string.not_available_text);
        }
        Date d = new Date(date*1000L);
        DateFormat df = DateFormat.getTimeInstance();
        return df.format(d);
    }

}
