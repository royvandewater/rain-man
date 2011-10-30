package com.royvandewater.rainman;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.tasks.AddressRequestTask;
import com.royvandewater.rainman.tasks.WeatherRequestTask;
import com.royvandewater.rainman.util.Callback;
import com.royvandewater.rainman.util.ErrorMessage;
import com.royvandewater.rainman.views.WeatherView;

public class RainManActivity extends Activity
{

    public static final String TAG = "rainman";

    private WeatherView view;
    private boolean requesting_address = false;
    private boolean requesting_weather = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.view = new WeatherView(this);
        this.view.initialize();

        requestLocation(new Callback() {

            public void call(Object error, Object location)
            {
                getZipcode((Location)location, new Callback() {

                    public void call(Object error, Object zipcode)
                    {
                        view.displayZipcode((String)zipcode);
                        checkWeather((String)zipcode, new Callback() {

                            public void call(Object error, Object weather)
                            {
                                Forecast forecast = (Forecast)weather;
                                view.displayWeather(forecast.getWeatherCondition());
                            }

                        });
                    }

                });
            }

        });
    }

    private void checkWeather(String zipcode, final Callback callback)
    {
        if (!requesting_weather) {
            requesting_weather = true;
            WeatherRequestTask task = new WeatherRequestTask(new Callback() {

                public void call(Object error, Object value)
                {
                    requesting_weather = false;

                    if (error != null)
                        view.displayError((ErrorMessage)error);
                    else
                        callback.call(error, value);
                }
            });
            task.execute(zipcode);
        }
    }

    /**
     * @param location to check the weather for
     * @param callback is called whenever a new location is received
     */
    public void getZipcode(Location location, final Callback callback)
    {
        if (!requesting_address) {
            requesting_address = true;
            AddressRequestTask task = new AddressRequestTask(new Callback() {
                public void call(Object error, Object value)
                {
                    requesting_address = false;

                    if (error != null)
                        view.displayError((ErrorMessage)error);
                    else
                        callback.call(error, value);
                }
            });
            task.execute(location);
        }
    }

    /**
     * @param callback - is called whenever a new location is received
     */
    private void requestLocation(final Callback callback)
    {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location)
            {
                callback.call(null, location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }
            public void onProviderEnabled(String provider)
            {
            }
            public void onProviderDisabled(String provider)
            {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

}
