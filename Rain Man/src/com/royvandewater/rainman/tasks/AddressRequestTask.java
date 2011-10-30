package com.royvandewater.rainman.tasks;

import java.io.IOException;
import org.json.JSONException;
import android.location.Location;
import android.os.AsyncTask;
import com.royvandewater.rainman.parsers.AddressParser;
import com.royvandewater.rainman.util.Callback;
import com.royvandewater.rainman.util.ErrorMessage;

public class AddressRequestTask extends AsyncTask<Location, String, String> {
    private ErrorMessage errorMessage;
    private Callback callback;

    public AddressRequestTask(Callback callback)
    {
        this.errorMessage = null;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Location... params)
    {
        try {
            return AddressParser.retrieveForLocation(params[0]).getPostalCode();
        } catch (IOException e) {
            errorMessage = new ErrorMessage(e, "Request for zipcode failed");
        } catch (JSONException e) {
            errorMessage = new ErrorMessage(e, "Request for zipcode failed, Malformed server response");
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(String result)
    {
        callback.call(errorMessage, result);
    }
    
    @Override
    protected void onCancelled()
    {
        callback.call(new ErrorMessage(null, "Zipcode task was cancelled"), null);
    }
}   