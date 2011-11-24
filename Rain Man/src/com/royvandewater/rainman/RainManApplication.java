package com.royvandewater.rainman;

import android.app.Application;

public class RainManApplication extends Application {

    public static final String TAG = "rainman";
    public static final String DATA = "data";
    public static final String NAME = "name";
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    }
    
    public static enum EventName {
        NOVALUE, LOCATION_UPDATE, ZIPCODE_UPDATE, WEATHER_UPDATE, ERROR, POLL_EVENT, PREFERENCES_UPDATE;

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
