package com.royvandewater.rainman;

import java.io.Serializable;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.tasks.AddressRequestTask;
import com.royvandewater.rainman.tasks.WeatherRequestTask;
import com.royvandewater.rainman.util.Callback;
import com.royvandewater.rainman.util.ErrorMessage;
import com.royvandewater.rainman.views.WeatherNotification;
import com.royvandewater.rainman.views.WeatherView;

public class RainManActivity extends Activity implements Handler.Callback
{
    private final ArrayList<Handler> handlers = new ArrayList<Handler>();
    private final WeatherView view = new WeatherView(this);
    private final WeatherNotification notification = new WeatherNotification(this);

    private boolean requesting_address = false;
    private boolean requesting_weather = false;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        registerHandler(new Handler(this));
        view.initialize();
        notification.initialize();
        findLocation();
    }
    
    public void registerHandler(Handler handler)
    {
        this.handlers.add(handler);
    }

    public boolean handleMessage(Message message)
    {
        Bundle bundle = message.getData();

        EventName eventName = EventName.toEventName(bundle.getString(RainManApplication.NAME));
        Object data = bundle.get(RainManApplication.DATA);
        
        switch (eventName) {
            case LOCATION_UPDATE:
                this.onLocationUpdate((Location)data);
                break;
            case ZIPCODE_UPDATE:
                this.onZipcodeUpdate((String)data);
                break;
            case WEATHER_UPDATE:
                this.onWeatherUpdate((Forecast)data);
                break;
            case ERROR:
                this.onError((ErrorMessage)data);
                break;
            case NOVALUE:
                break;
        }
        return false;
    }

    private void onLocationUpdate(Location location)
    {
        view.displayGpsCoordinates(location.getLongitude() + ", " + location.getLatitude());
        findZipcode(location);
    }

    private void onZipcodeUpdate(String zipcode)
    {
        view.displayZipcode((String)zipcode);
        findWeather(zipcode);
    }
    
    private void onWeatherUpdate(Forecast forecast) {
        view.displayWeather(forecast.getWeatherCondition());
        notification.displayWeather(forecast.getWeatherCondition());
    }
    
    private void onError(ErrorMessage errorMessage)
    {
        view.displayError(errorMessage);
    }
    
    private void findLocation()
    {
        final LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            public void onLocationChanged(Location location)
            {
                sendMessage(EventName.LOCATION_UPDATE, location);
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
                        sendMessage(EventName.ERROR, error);
                    else
                        sendMessage(EventName.ZIPCODE_UPDATE, (String)value);
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
                        sendMessage(EventName.ERROR, error);
                    else
                        sendMessage(EventName.WEATHER_UPDATE, (Forecast)value);
                }
            });
            task.execute(zipcode);
        }
    }
    
    private void sendMessage(EventName eventName, Serializable serializable)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RainManApplication.DATA, serializable);

        sendMessage(eventName, bundle);
    }

    private void sendMessage(EventName eventName, Parcelable parcelable)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RainManApplication.DATA, parcelable);

        sendMessage(eventName, bundle);
    }

    private void sendMessage(EventName eventName, String string)
    {
        Bundle bundle = new Bundle();
        bundle.putString(RainManApplication.DATA, string);

        sendMessage(eventName, bundle);
    }

    private void sendMessage(EventName eventName, Bundle bundle)
    {
        bundle.putString(RainManApplication.NAME, eventName.toString());

        for (Handler handler : handlers) {
            Message message = Message.obtain();
            message.setData(bundle);

            handler.sendMessage(message);
        }

    }
    
    private static enum EventName {
        NOVALUE, LOCATION_UPDATE, ZIPCODE_UPDATE, WEATHER_UPDATE, ERROR;

        public static EventName toEventName(String str)
        {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception e) {
                return NOVALUE;
            }
        }
    }
}
