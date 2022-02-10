package com.nehaanand.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String appId = "5242302cc733499b79b9d9081f1fa0b6";
    final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";

    final long min_time = 5000;
    final float min_distance = 1000;
    final int request_code = 101;

    String location_provider = LocationManager.GPS_PROVIDER;

    TextView nameofcity, weatherstate, temperature;
    ImageView mweatherIcon;
    RelativeLayout mCityFinder;
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherstate = findViewById(R.id.weather_condition);
        temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weather_icon);
        mCityFinder = findViewById(R.id.city_finder);
        nameofcity = findViewById(R.id.city_name);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("apiid", appId);
                letsdoSomenetworking(params);



            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, request_code);
            return;
        }
        mLocationManager.requestLocationUpdates(location_provider, min_time, min_distance, mLocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == request_code){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Location got Successfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }else{
                // user denied the permission

            }
        }
    }

    private void letsdoSomenetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(weatherUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                Toast.makeText(MainActivity.this, "Data received successfully.", Toast.LENGTH_SHORT).show();
                weather_data weatherD = weather_data.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


    }

    private void updateUI(weather_data weather){
        temperature.setText(weather.getmTemperature());
        nameofcity.setText(weather.getmCity());
        weatherstate.setText(weather.getmWeatherType());
        int resourceID = getResources().getIdentifier(weather.getMicon(), "drawable", getPackageName());
        mweatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}