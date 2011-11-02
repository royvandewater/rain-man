package com.royvandewater.rainman.views;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.royvandewater.rainman.R;
import com.royvandewater.rainman.RainManApplication;
import com.royvandewater.rainman.util.ErrorMessage;

public class WeatherView
{
    private Activity activity;

    public WeatherView(Activity activity) {
        this.activity = activity;
    }
    
    public void initialize() {
        activity.setContentView(R.layout.main);
    }
    
    public void displayGpsCoordinates(String gpsCoordinates)
    {
        TextView gpsCoordinatesText = (TextView)activity.findViewById(R.id.gps_coordinates);
        gpsCoordinatesText.setText(gpsCoordinates);
    }

    public void displayZipcode(String zipcode)
    {
        TextView zipcodeText = (TextView)activity.findViewById(R.id.zipcode);
        zipcodeText.setText(zipcode);
    }
    
    public void displayWeather(String weather)
    {
        TextView weatherText = (TextView)activity.findViewById(R.id.weather);
        weatherText.setText(weather);
        
    }
    
    public void displayError(ErrorMessage errorMessage)
    {
        if(errorMessage.getException() != null)
            Log.e(RainManApplication.TAG, errorMessage.getException().getStackTrace().toString());
        
        Toast.makeText(activity, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
    }
}