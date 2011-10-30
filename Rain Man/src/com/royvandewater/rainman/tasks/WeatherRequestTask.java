package com.royvandewater.rainman.tasks;

import java.io.IOException;
import android.os.AsyncTask;
import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.parsers.GoogleForecastParser;
import com.royvandewater.rainman.util.Callback;
import com.royvandewater.rainman.util.ErrorMessage;

public class WeatherRequestTask extends AsyncTask<String, String, Forecast>
{
    private ErrorMessage errorMessage;
    private Callback callback;

    public WeatherRequestTask(Callback callback)
    {
        this.errorMessage = null;
        this.callback = callback;
    }

    @Override
    protected Forecast doInBackground(String... params)
    {
        String zipcode = params[0];
        try {
            return GoogleForecastParser.retrieveForZipcode(zipcode);
        } catch (IOException e) {
            errorMessage = new ErrorMessage(e, "Request for weather failed");
            e.printStackTrace();
        } catch (Exception e) {
            errorMessage = new ErrorMessage(e, "Request for weather failed, Malformed server response");
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(Forecast forecast)
    {
        callback.call(errorMessage, forecast);
    }
    
    @Override
    protected void onCancelled()
    {
        callback.call(new ErrorMessage(null, "Weather task was cancelled"), null);
    }
}
