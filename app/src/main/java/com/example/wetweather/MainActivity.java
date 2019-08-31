package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.sync.ScheduledUpdateWorker;
import com.example.wetweather.sync.WeatherSyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView mWeather;
    private TextView mTime;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeather = findViewById(R.id.weathertext);
        mTime = findViewById(R.id.time1);

        setupViewModel();
        //scheduleWeatherUpdate(1);

        if (isNetworkAvailable()) {
            WeatherSyncUtils.startImmediateSync(this);
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                display(weatherEntries);
            }
        });
    }

    private void display(List<WeatherItem> weatherEntries){
        if (weatherEntries.size()>0){
            Log.i(TAG,weatherEntries.size()+"toloka");
        WeatherItem ebt = weatherEntries.get(0);
        mWeather.setText(ebt.getSummary());
        mTime.setText(ebt.getDateTimeMillis()+"");}
    }

    private void displayz(WeatherItem item){
        mWeather.setText(item.getSummary());
    }

    //Helper method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void scheduleWeatherUpdate(int repeatInterval){

        // Create a Constraints object that defines when the task should run
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest syncWeather =
                new PeriodicWorkRequest.Builder(ScheduledUpdateWorker.class, repeatInterval, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance()
                .enqueue(syncWeather);

    }
}
