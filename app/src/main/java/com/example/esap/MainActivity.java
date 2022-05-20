package com.example.esap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn;
    private LocationRequest locationRequest;
    private static String serviceType;
    private String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (AppCompatButton) findViewById(R.id.button);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /*locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
                myIntent.putExtra("type","");
                //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
    public void onClickAccident(View view){
        ImageButton e_btn = findViewById(R.id.imageButton3);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="ambulance";
        text = "حادثہ";
        getCurrentLocation();

    }

    public void onClickSuffocation(View view){
        ImageButton e_btn = findViewById(R.id.imageButton5);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="ambulance";
        text = "دم گھٹنا";
        getCurrentLocation();
    }

    public void onClickFire(View view){
        ImageButton e_btn = findViewById(R.id.imageButton7);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="firebrigade";
        text = "آگ";
        getCurrentLocation();
    }

    public void onClickMaternity(View view){
        ImageButton e_btn = findViewById(R.id.imageButton6);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="ambulance";
        text = "زچہ و بچہ";
        getCurrentLocation();
    }

    public void onClickFoodPoison(View view){
        ImageButton e_btn = findViewById(R.id.imageButton8);
        e_btn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="فوڈ پوائزن";
        text = "food poisoning";
        getCurrentLocation();
    }

    public void onClickAttack(View view){
        ImageButton accbtn = findViewById(R.id.imageButton4);
        accbtn.setBackgroundResource(R.drawable.button_shadow);
        serviceType="ambulance";
        text = "دل کا دورہ";
        getCurrentLocation();
    }
    public void onClickFireBrigade(View view){
        ImageButton fire_btn = findViewById(R.id.imageButton12);
        Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
        myIntent.putExtra("type","firebrigade");
        //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }

    public void onClickAmbulance(View view){
        Intent myIntent = new Intent(MainActivity.this, SpeechActivity.class);
        myIntent.putExtra("type","ambulance");
        //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }


    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        Log.i("Location: ","Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                        Intent myIntent = new Intent(MainActivity.this, MessageInfo2.class);
                                        myIntent.putExtra("key", serviceType);
                                        myIntent.putExtra("key2", latitude);
                                        myIntent.putExtra("key3", longitude);
                                        myIntent.putExtra("key4", text);//Optional parameters
                                        MainActivity.this.startActivity(myIntent);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                        .LENGTH_SHORT).show();
                finish();
            }

    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

}