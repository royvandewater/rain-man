package com.royvandewater.rainman;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.royvandewater.rainman.RainManApplication.EventName;
import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.tasks.AddressRequestTask;
import com.royvandewater.rainman.tasks.PollTask;
import com.royvandewater.rainman.tasks.WeatherRequestTask;
import com.royvandewater.rainman.util.Callback;
import com.royvandewater.rainman.util.ErrorMessage;
import com.royvandewater.rainman.util.EventBus;
import com.royvandewater.rainman.views.RainmanNotification;

public class WeatherService extends Service implements Handler.Callback
{
    private final EventBus eventBus = EventBus.obtain();
    private final RainmanNotification notification = new RainmanNotification(this);

    private boolean requesting_address = false;
    private boolean requesting_weather = false;
    private PollTask task;

    private static final int DEFAULT_POLLING_INTERVAL = 15;

    @Override
    public void onCreate()
    {
        super.onCreate();

        eventBus.registerHandler(new Handler(this));
        notification.initialize();

        onPrefencesUpdate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        task.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public boolean handleMessage(Message message)
    {
        Bundle bundle = message.getData();

        EventName eventName = EventName.toEventName(bundle.getString(EventBus.EVENT_NAME));
        Object data = bundle.get(EventBus.EVENT_DATA);

        switch (eventName) {
            case POLL_EVENT:
                onPollEvent();
                break;
            case LOCATION_UPDATE:
                this.onLocationUpdate((Location)data);
                break;
            case ZIPCODE_UPDATE:
                this.onZipcodeUpdate((String)data);
                break;
            case WEATHER_UPDATE:
                this.onWeatherUpdate((Forecast)data);
                break;
            case PREFERENCES_UPDATE:
                this.onPrefencesUpdate();
                break;
            case ERROR:
                this.onError((ErrorMessage)data);
                break;
            case NOVALUE:
                break;
        }
        return false;
    }

    private void onPollEvent()
    {
        findLocation();
    }

    private void onLocationUpdate(Location location)
    {
        findZipcode(location);
    }

    private void onZipcodeUpdate(String zipcode)
    {
        findWeather(zipcode);
    }

    private void onWeatherUpdate(Forecast forecast)
    {
        String condition = forecast.getWeatherCondition();

        if (condition.toLowerCase().contains("rain") || condition.toLowerCase().contains("storm"))
            notification.displayWeather(forecast.getWeatherCondition());
    }

    private void onPrefencesUpdate()
    {
        if (task != null)
            task.cancel(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checkWeather = preferences.getBoolean(getString(R.string.check_weather_preference), true);
        int pollingInterval = preferences.getInt(getString(R.string.polling_interval_preference), DEFAULT_POLLING_INTERVAL);

        if (checkWeather) {
            task = new PollTask(pollingInterval, EventName.POLL_EVENT.toString());
            task.execute();
        }
    }

    private void onError(ErrorMessage errorMessage)
    {
        if (errorMessage.getException() != null)
            Log.e(RainManApplication.TAG, errorMessage.getException().getStackTrace().toString());

        Toast.makeText(this, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void findLocation()
    {
        final LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            notification.displayWeather("Please enable Network Location in Android Preferences or disable 'Check Weather'");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            public void onLocationChanged(Location location)
            {
                eventBus.sendMessage(EventName.LOCATION_UPDATE.toString(), location);
                locationManager.removeUpdates(this);
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
        });
    }

    private void findZipcode(Location location)
    {
        if (!requesting_address) {
            requesting_address = true;
            AddressRequestTask task = new AddressRequestTask(new Callback() {
                public void call(ErrorMessage error, Object value)
                {
                    requesting_address = false;

                    if (error != null)
                        eventBus.sendMessage(EventName.ERROR.toString(), error);
                    else
                        eventBus.sendMessage(EventName.ZIPCODE_UPDATE.toString(), (String)value);
                }
            });
            task.execute(location);
        }
    }

    private void findWeather(String zipcode)
    {
        if (!requesting_weather) {
            requesting_weather = true;
            WeatherRequestTask task = new WeatherRequestTask(new Callback() {

                public void call(ErrorMessage error, Object value)
                {
                    requesting_weather = false;

                    if (error != null)
                        eventBus.sendMessage(EventName.ERROR.toString(), error);
                    else
                        eventBus.sendMessage(EventName.WEATHER_UPDATE.toString(), (Forecast)value);
                }
            });
            task.execute(zipcode);
        }
    }
}
