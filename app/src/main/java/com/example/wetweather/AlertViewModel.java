package com.example.wetweather;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wetweather.db.WeatherItem;

import java.util.List;

public class AlertViewModel extends AndroidViewModel {

    private LiveData<List<WeatherItem>> mWeathers;
    private WeatherRepository mRepository;

    public AlertViewModel(Application application) {
        super(application);
        mRepository = WeatherRepository.getInstance(application.getApplicationContext());
        mWeathers = mRepository.getAlerts();
    }

    public LiveData<List<WeatherItem>> getAlerts() {
        return mWeathers;
    }
}
