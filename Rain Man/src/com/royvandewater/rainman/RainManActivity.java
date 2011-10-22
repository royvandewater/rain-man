package com.royvandewater.rainman;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.royvandewater.rainman.util.AddressParser;

public class RainManActivity extends Activity {
    
    private static final String TAG = "rainman";
    private final static String GEOLOCATION_URL = "http://maps.google.com/maps/geo?ll=";
    private boolean requesting_address = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        requestLocation();
    }


    public void checkForRain(Location location) {
        if(!requesting_address) {
            requesting_address = true;
            AddressRequestTask task = new AddressRequestTask();
            task.execute(location);
        }
    }

    private Address getAddressFromLocation(Location location)
    {
        Address address = new Address(AddressParser.DEFAULT_LOCALE);

        AddressParser addressParser = new AddressParser();
        
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String uri = GEOLOCATION_URL + latitude + "," + longitude;
        
        InputStream geoStream;
        try {
            geoStream = getGeocodedInformation(uri);
            address = addressParser.parse(geoStream);
        } catch (ClientProtocolException e) {
            displayError(e, "Request for zipcode failed");
        } catch (IOException e) {
            displayError(e, "Request for zipcode failed");
        } catch (JSONException e) {
            displayError(e, "Request for zipcode failed, Malformed server response");
        }
        return address;
    }

    private void displayError(Exception e, String message)
    {
        Log.e(TAG, e.getStackTrace().toString());
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private InputStream getGeocodedInformation(String uri) throws IOException, ClientProtocolException
    {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        HttpResponse httpResponse = client.execute(httpGet);
        InputStream zipStream = httpResponse.getEntity().getContent();
        return zipStream;
    }

    /**
     * Calls 'checkForRain' whenever a new updated location is received
     */
    private void requestLocation()
    {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              checkForRain(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
          };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    
    
    private class AddressRequestTask extends AsyncTask<Location, String, String> {
        @Override
        protected String doInBackground(Location... params)
        {
            Address address = getAddressFromLocation(params[0]);
            return address.getPostalCode();
        }
        
        @Override
        protected void onPostExecute(String result)
        {
            requesting_address = false;
            ((TextView) findViewById(R.id.zipcode)).setText(result);
        }
        
        @Override
        protected void onCancelled()
        {
            requesting_address = false;
            Log.w(TAG, "Zipcode task was cancelled");
        }
    }    
}