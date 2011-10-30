package com.royvandewater.rainman.util;

import java.io.IOException;
import org.json.JSONException;
import android.location.Location;
import android.os.AsyncTask;
import com.royvandewater.rainman.models.Address;

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
            return Address.buildFromLocation(params[0]).getPostalCode();
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