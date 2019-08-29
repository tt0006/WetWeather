package com.example.wetweather.sync;

import android.content.Context;

import com.example.wetweather.Data.WeatherItem;
import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherEntity;
import com.example.wetweather.utils.NetworkUtils;

public class WeatherSyncTask {

    synchronized public static void syncWeather(Context context) {

        try {
            WeatherItem newData = NetworkUtils.extractJSONrequest();


            WeatherDB db = WeatherDB.getInstance(context);
            if (newData != null) {
                //delete all data in db
                db.weatherDao().deleteAll();

                //insert newData to db
                WeatherEntity ent = new WeatherEntity(newData.getDateTimeMillis(), newData.getSummary(),
                        newData.getIcon(), newData.getPressure(), newData.getHumidity(), newData.getWindSpeed(),
                        newData.getWindDirection(), newData.getmTemperature());

                db.weatherDao().insertWeatherItem(ent);
            }


        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }

    }
}
