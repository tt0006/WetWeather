package com.example.wetweather;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<WeatherEntity>> weathers;

    public MainViewModel(Application application) {
        super(application);
        WeatherDB database = WeatherDB.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving weather from the DataBase");
        weathers = database.weatherDao().loadAll();
    }

    public LiveData<List<WeatherEntity>> getTasks() {
        return weathers;
    }
}
