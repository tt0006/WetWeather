package com.example.wetweather.utils;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.wetweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class WetWeatherUtils {
    private static final String LOG_TAG = WetWeatherUtils.class.getSimpleName();


    /**
     * format the temperature so that no decimal points show. Temperatures will be formatted
     * to the following form: "21°"
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees
     * @return Formatted temperature String in the following form:
     * "21°"
     */
    public static String formatTemperature(Context context, String temperature) {
        if (null == temperature) {
            return "";
        }
        int temperatureFormatResourceId = R.string.format_temperature;
        double temp;
        try {
            temp = Double.parseDouble(temperature);
        } catch (NumberFormatException e) {
            temp = 0.0;
        }

        /* Assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temp);
    }


    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param degrees   Degrees as measured on a compass
     *                  See https://www.mathsisfun.com/geometry/degrees.html
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
     * @param degrees Degrees as measured on a compass
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
     * @return Weather icon
     */
    public static int getResourceIconIdForWeatherCondition(String weatherIcon) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night

        switch (weatherIcon) {
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


    /**
     * This method returns formatted date as String like "1:00 (Wed)"
     *
     * @param dateInSeconds date in seconds to convert to String
     * @return formatted date as String like "1:00 (Wed)"
     */
    public static String getHourWithDay(long dateInSeconds) {
        long dateInMillis = dateInSeconds * 1000L;
        SimpleDateFormat dayFormat = new SimpleDateFormat("H:mm (E)");
        return dayFormat.format(dateInMillis);
    }

    /**
     * This method returns formatted date as String like "Today", "Tomorrow", "Friday, February 21"
     *
     * @param context       Android Context to access preferences and resources
     * @param dateInSeconds Date in seconds to convert to String
     * @return Formatted date as String like "Today", "Tomorrow", "Friday, February 21"
     */
    public static String getDayName(Context context, long dateInSeconds) {
        /*
         * If the date is today or tomorrow, return the localized version of "Today" or "Tomorrow"
         * instead of the actual day name.
         */
        long dateInMilliSeconds = dateInSeconds * 1000L;

        if (DateUtils.isToday(dateInMilliSeconds)) {
            return context.getString(R.string.today);
        }
        // Check if OS version is Oreo or newer before use LocalDate as earlier doesn't support it.
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate passedDay = LocalDateTime.ofEpochSecond(dateInSeconds, 0, ZoneOffset.UTC).toLocalDate();
            LocalDate today = LocalDate.now();

            if (passedDay.isEqual(today.plusDays(1))) {
                return context.getString(R.string.tomorrow);
            }
        }

        // return formatted date by default
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, LLLL d");
        return dayFormat.format(dateInMilliSeconds);

    }

    /**
     * This method returns formatted time as String like "Updated: 9:30" for today's time
     * or "Updated: February, 21 9:30" for past
     *
     * @param context       Android Context to access preferences and resources
     * @param dateInSeconds Date in seconds to convert to String
     * @return Formatted time as String
     */
    public static String getUpdateTime(Context context, long dateInSeconds) {

        long dateInMillis = dateInSeconds * 1000L;

        Date d = new Date(dateInMillis);
        DateFormat df;

        if (DateUtils.isToday(dateInMillis)) {
            df = DateFormat.getTimeInstance();
        } else {
            df = DateFormat.getDateTimeInstance();
        }
        return String.format("%1$s %2$s", context.getString(R.string.updated_at_text), df.format(d));
    }

    /**
     * This method returns formatted time as String like "9:30" or "N/A" if it 0
     *
     * @param context       Android Context to access preferences and resources
     * @param dateInSeconds Date in seconds to convert to String
     * @return Formatted time as String
     */
    public static String getTime(Context context, long dateInSeconds) {
        if (dateInSeconds == 0L) {
            return context.getString(R.string.not_available_text);
        }
        Date d = new Date(dateInSeconds * 1000L);
        DateFormat df = DateFormat.getTimeInstance();
        return df.format(d);
    }

}
