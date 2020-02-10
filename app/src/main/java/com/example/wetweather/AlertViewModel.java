package com.example.wetweather;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class AlertViewModel extends AndroidViewModel {

    private LiveData<List<WeatherItem>> mWeathers;

    public AlertViewModel(Application application) {
        super(application);
        WeatherRepository repository = WeatherRepository.getInstance(application.getApplicationContext());
        mWeathers = repository.getAlerts();
    }

    public LiveData<List<WeatherItem>> getAlerts() {
        return mWeathers;
    }
}
