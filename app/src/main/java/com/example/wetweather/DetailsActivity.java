package com.example.wetweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.sync.WeatherSyncUtils;
import com.example.wetweather.utils.NetworkUtils;
import com.example.wetweather.utils.WetWeatherUtils;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private DetailsAdapter mDetailsAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        final SwipeRefreshLayout swipeToRefresh = findViewById(R.id.swipeToRefreshForecast);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mDetailsAdapter = new DetailsAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mDetailsAdapter);

        Intent intent = getIntent();

        final long minTime = intent.getLongExtra("TIME", 0);

        //set activity title using location name
        setTitle(WetWeatherUtils.getDayName(this, minTime));

        showLoading();

        //need to change it to support daytime change dates
        setupViewModel(minTime, minTime+82800);


        final Context mActivityContext = this;
        //set swipe to refresh
        swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (NetworkUtils.isNetworkAvailable(mActivityContext)) {
                            WeatherSyncUtils.startImmediateSync(mActivityContext);
                        } else {
                            Toast.makeText(mActivityContext, R.string.network_not_available,
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeToRefresh.setRefreshing(false);
                    }
                }
        );
    }

    private void setupViewModel(long min, long max) {
        final DetailsViewModel viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
        viewModel.getDetailsWeather(min, max).observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                //viewModel.getHourlyWeather().removeObserver(this);
                mDetailsAdapter.setWeatherData(weatherEntries);
                showWeatherDataView();
            }
        });
    }

    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
