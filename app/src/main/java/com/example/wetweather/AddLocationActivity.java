package com.example.wetweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.example.wetweather.prefs.WetWeatherPreferences;
import com.example.wetweather.sync.WeatherSyncUtils;

import java.util.ArrayList;

public class AddLocationActivity extends AppCompatActivity {

    private AddLocationActivity.AddressListResultReceiver addressResultReceiver;
    private EditText addressNameTv;
    private ListView addressListView;
    private Context mContext;
    private static final String TAG = AddLocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        addressNameTv = findViewById(R.id.address_hint);
        addressListView = findViewById(R.id.addresses_lst);
        addressResultReceiver = new AddressListResultReceiver(new Handler());
        mContext = getApplicationContext();
    }

    public void getAddressesByName(View view){
        getAddresses(addressNameTv.getText().toString());
    }

    private void getAddresses(String addName) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(AddLocationActivity.this,
                    "Can't find address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AddressesByNameIntentService.class);
        intent.putExtra("address_receiver", addressResultReceiver);
        intent.putExtra("address_name", addName);
        startService(intent);
    }

    private class AddressListResultReceiver extends ResultReceiver {
        AddressListResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                Toast.makeText(AddLocationActivity.this,
                        "Enter address name, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (resultCode == 1) {
                Toast.makeText(AddLocationActivity.this,
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<Address> addresses = resultData.getParcelableArrayList("addressesList");

            String[] addressList = new String[addresses.size()] ;
            int j =0;
            for(Address address : addresses){
                ArrayList<String> addressInfo = new ArrayList<>();
                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressInfo.add(address.getAddressLine(i));
                }
                addressList[j] = TextUtils.join(System.getProperty("line.separator"),
                        addressInfo);
                Log.d(TAG,addressList[j]);
                j++;
            }

            if (addresses.size()>0) {
                Address address = addresses.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();

                WetWeatherPreferences.updateLocationName(mContext, address.getLocality());
                WetWeatherPreferences.setLatitudeLongitude(mContext, lat, lng);

                showResults(addressList);

                WeatherSyncUtils.startImmediateSync(mContext);
                finish();
            }
        }
    }

    private void showResults(String[] addressList){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(arrayAdapter);
    }
}