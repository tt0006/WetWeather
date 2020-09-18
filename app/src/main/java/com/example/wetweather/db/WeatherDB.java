package com.example.wetweather.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database class from Architecture Components
 * Room is a database layer on top of SQLite database. It uses DAO to issue queries to its database.
 * We only need one instance of the Room database for the whole app.
 */
@Database(entities = {WeatherItem.class}, version = 16, exportSchema = false)
public abstract class WeatherDB extends RoomDatabase {

    private static final String LOG_TAG = WeatherDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weather";

    // WeatherDB as a singleton to prevent multiple instances of database opened at the same time
    private static WeatherDB sInstance;

    public static WeatherDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDB.class, WeatherDB.DATABASE_NAME)
                        .fallbackToDestructiveMigration() //Wipes and rebuilds instead of migrating
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    // Define the DAO that works with the database.
    public abstract WeatherDao weatherDao();
}
