package com.example.wetweather.sync;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.wetweather.utils.NetworkUtils;

public class ScheduledUpdateWorker extends Worker {

    public ScheduledUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (NetworkUtils.updateWeatherData(getApplicationContext())){
            return Result.success();
        } else
            return Result.failure();
    }
}
