package com.example.wetweather.utils;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import com.example.wetweather.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
     * This method uses the moon number to determine moon phase.
     *
     * @param num moon number
     * @return moon phase as String
     */
    public static String getMoonPhase(Context context, String num) {
        String moonPhase = "";
        double number = getDoubleFromString(num);

        if (number == -1.0) {
            return moonPhase;
        }

        if (number == 0) {
            moonPhase = context.getString(R.string.moon_phase_new_label);
        } else if (number > 0 & number < 0.25) {
            moonPhase = context.getString(R.string.moon_phase_waxing_crescent_label);
        } else if (number == 0.25) {
            moonPhase = context.getString(R.string.moon_phase_first_quarter_label);
        } else if (number > 0.25 & number < 0.5) {
            moonPhase = context.getString(R.string.moon_phase_waxing_gibbous_label);
        } else if (number == 0.5) {
            moonPhase = context.getString(R.string.moon_phase_full_label);
        } else if (number > 0.5 & number < 0.75) {
            moonPhase = context.getString(R.string.moon_phase_waning_gibbous_label);
        } else if (number == 0.75) {
            moonPhase = context.getString(R.string.moon_phase_last_quarter_label);
        } else if (number > 0.75) {
            moonPhase = context.getString(R.string.moon_phase_waning_crescent_label);
        }

        return moonPhase;
    }

    /**
     * This method uses the moon number to determine moon phase.
     *
     * @param num moon number
     * @return moon phase as icon
     */
    public static int getMoonPhaseIcon(String num) {
        int moonPhase = R.drawable.ic_moon_full_moon;
        double number = getDoubleFromString(num);

        if (number == -1.0) {
            return moonPhase;
        }

        if (number == 0) {
            moonPhase = R.drawable.ic_moon_new;
        } else if (number > 0 & number < 0.25) {
            moonPhase = R.drawable.ic_moon_waxing_crescent;
        } else if (number == 0.25) {
            moonPhase = R.drawable.ic_moon_first;
        } else if (number > 0.25 & number < 0.5) {
            moonPhase = R.drawable.ic_moon_waxing_gibbous;
        } else if (number == 0.5) {
            moonPhase = R.drawable.ic_moon_full_moon;
        } else if (number > 0.5 & number < 0.75) {
            moonPhase = R.drawable.ic_moon_waning_gibbous;
        } else if (number == 0.75) {
            moonPhase = R.drawable.ic_moon_last;
        } else if (number > 0.75) {
            moonPhase = R.drawable.ic_moon_waning_crescennt;
        }

        return moonPhase;
    }

    /**
     * This method converts String to double
     *
     * @param value String value
     * @return converted to double value or -1.0
     */
    public static double getDoubleFromString(String value){
        double number;
        if (value == null){
            return -1.0;
        }
        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -1.0;
        }
        return number;
    }


    /**
     * This method uses the weather description to determine its icon.
     *
     * @param weatherIcon weather description as per API documentation
     * @return Weather icon
     */
    public static int getResourceIconIdForWeatherCondition(String weatherIcon, double precipIntensity) {
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, partly-cloudy-night

        switch (weatherIcon) {
            case "clear-day":
                return R.drawable.ic_clear_day_vector;
            case "clear-night":
                return R.drawable.ic_clear_night_vector;
            case "rain":
                if (precipIntensity < 2.5){
                    return R.drawable.ic_rain_light;
                } else if (precipIntensity < 5.0){
                    return R.drawable.ic_rain_moderate;
                } else if (precipIntensity < 10.0){
                    return R.drawable.ic_rain_heavy;
                }
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
            LocalDate passedDay = LocalDateTime.ofEpochSecond(dateInSeconds, 0, OffsetDateTime.now().getOffset()).toLocalDate();
            LocalDate today = LocalDate.now();

            if (passedDay.isEqual(today.plusDays(1))) {
                return context.getString(R.string.tomorrow);
            }
        }

        // return formatted date by default
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, d MMMM");
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
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(d);
    }

}
