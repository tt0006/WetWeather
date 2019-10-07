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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wetweather.db.WeatherItem;
import com.example.wetweather.sync.WeatherSyncUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ForecastAdapter mForecastAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private Context mActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        getSupportActionBar().setElevation(0f);

        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        final SwipeRefreshLayout swipeToRefresh = findViewById(R.id.swiperefresh);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mActivityContext = this;

        mForecastAdapter = new ForecastAdapter(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mForecastAdapter);

        showLoading();

        setupViewModel();

        if (isNetworkAvailable()) {
            WeatherSyncUtils.initialize(this);
        } else{
            Toast.makeText(mActivityContext, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        //set swipe to refresh
        swipeToRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkAvailable()) {
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
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getWeathers().observe(this, new Observer<List<WeatherItem>>() {
            @Override
            public void onChanged(@Nullable List<WeatherItem> weatherEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mForecastAdapter.setWeatherData(weatherEntries);
                showWeatherDataView();
            }
        });
    }

    //Helper method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showWeatherDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This is where we inflate and set up the menu for this Activity
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int position) {
        if (mForecastAdapter.presentAlert) {
            if (position == 0) {
                Intent intent = new Intent(this, AlertActivity.class);
                startActivity(intent);
                return;
            }
            if (position == 3) {
                Intent intent = new Intent(this, HourlyForecastActivity.class);
                startActivity(intent);
            }
            if (position > 1 && position < 5) {
                return;
            }
        } else {

            if (position == 2) {
                Intent intent = new Intent(this, HourlyForecastActivity.class);
                startActivity(intent);
                return;
            }
            if (position > 0 && position < 4) {
                return;
            }
        }
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }
}
