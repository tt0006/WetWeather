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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.prefs.WetWeatherPreferences;
import com.example.wetweather.sync.WeatherSyncUtils;
import com.example.wetweather.utils.NetworkUtils;

import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private static final String TAG = LocationActivity.class.getSimpleName();

    private LocationAdapter mLocationAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        //set activity title using location name
        setTitle(WetWeatherPreferences.getPreferencesLocationName(this));

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        final SwipeRefreshLayout swipeToRefresh = findViewById(R.id.swipeToRefreshForecast);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mLocationAdapter = new LocationAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mLocationAdapter);

        setupViewModel();

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

    private void setupViewModel() {
        LocationViewModel viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                mLocationAdapter.setWeatherData(weatherEntries);
            }
        });
    }

    // Menu block-----------------------------------------------------------------------------------
    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graphs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.GraphsBtn) {
            // start graphs activity
            Intent graphsActivityIntent = new Intent(LocationActivity.this, GraphsActivity.class);
            startActivity(graphsActivityIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    // Menu block end-------------------------------------------------------------------------------
}
