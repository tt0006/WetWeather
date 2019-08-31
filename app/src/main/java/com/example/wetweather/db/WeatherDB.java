package com.example.wetweather.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherItem.class}, version = 1)
public abstract class WeatherDB extends RoomDatabase {

    private static final String LOG_TAG = WeatherDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weather";
    private static WeatherDB sInstance;

    public static WeatherDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDB.class, WeatherDB.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract WeatherDao weatherDao();
}
