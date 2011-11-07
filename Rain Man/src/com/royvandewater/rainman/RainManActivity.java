package com.royvandewater.rainman;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.royvandewater.rainman.RainManApplication.EventName;
import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.util.ErrorMessage;
import com.royvandewater.rainman.util.EventBus;
import com.royvandewater.rainman.views.WeatherView;

public class RainManActivity extends Activity implements Handler.Callback
{
    
    private final WeatherView view = new WeatherView(this);
    private final EventBus eventBus = EventBus.obtain();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        eventBus.registerHandler(new Handler(this));
        view.initialize();
        
        Intent intent = new Intent(this, WeatherService.class);
        startService(intent);
    }
    
    
    public boolean handleMessage(Message message)
    {
        Bundle bundle = message.getData();

        EventName eventName = EventName.toEventName(bundle.getString(EventBus.EVENT_NAME));
        Object data = bundle.get(EventBus.EVENT_DATA);
        
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
    }

    private void onZipcodeUpdate(String zipcode)
    {
        view.displayZipcode((String)zipcode);
    }
    
    private void onWeatherUpdate(Forecast forecast) {
        view.displayWeather(forecast.getWeatherCondition());
    }
    
    private void onError(ErrorMessage errorMessage)
    {
        view.displayError(errorMessage);
    }
}
