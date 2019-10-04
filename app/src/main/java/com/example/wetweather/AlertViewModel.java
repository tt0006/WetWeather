package com.example.wetweather;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherDB;
import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class AlertViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = AlertViewModel.class.getSimpleName();

    private LiveData<List<WeatherItem>> weathers;

    public AlertViewModel(Application application) {
        super(application);
        WeatherDB database = WeatherDB.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving alerts from the DataBase");
        weathers = database.weatherDao().loadAlerts();
    }

    public LiveData<List<WeatherItem>> getAlerts() {
        return weathers;
    }
}
