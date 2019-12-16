package com.example.wetweather;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.os.ResultReceiver;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AddressesByNameIntentService extends IntentService {

    private static final String TAG = AddressesByNameIntentService.class.getSimpleName();
    private ResultReceiver addressResultReceiver;

    public AddressesByNameIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String msg = "";
        addressResultReceiver = intent.getParcelableExtra("address_receiver");

        if (addressResultReceiver == null) {
            Log.e(TAG, "No receiver in intent");
            return;
        }

        String addressName = intent.getStringExtra("address_name");

        if (addressName == null) {
            msg = "No name found";
            sendResultsToReceiver(0, msg, null);
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(addressName, 5);
        } catch (Exception ioException) {
            Log.e("", "Error in getting addresses for the given name");
        }

        if (addresses == null || addresses.size()  == 0) {
            msg = "No address found for the address name";
            sendResultsToReceiver(1, msg, null);
        } else {
            Log.d(TAG, "number of addresses received "+addresses.size());
            sendResultsToReceiver(2,"", new ArrayList<>(addresses));
        }
    }

    private void sendResultsToReceiver(int resultCode, String message, ArrayList<Address> addresses) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", message);
        bundle.putParcelableArrayList("addressesList", addresses);
        addressResultReceiver.send(resultCode, bundle);
    }
}