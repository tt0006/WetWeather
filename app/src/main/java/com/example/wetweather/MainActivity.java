package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.wetweather.Data.WeatherItem;
import com.example.wetweather.db.WeatherEntity;
import com.example.wetweather.sync.WeatherSyncUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mWeather;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeather = findViewById(R.id.weathertext);

        setupViewModel();

        if (isNetworkAvailable()) {
            WeatherSyncUtils.startImmediateSync(this);
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<WeatherEntity>>() {
            @Override
            public void onChanged(@Nullable List<WeatherEntity> weatherEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                display(weatherEntries);
            }
        });
    }

    private void display(List<WeatherEntity> weatherEntries){
        if (weatherEntries.size()>0){
            Log.i(TAG,weatherEntries.size()+"toloka");
        WeatherEntity ebt = weatherEntries.get(0);
        mWeather.setText(ebt.getSummary());}
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
}
