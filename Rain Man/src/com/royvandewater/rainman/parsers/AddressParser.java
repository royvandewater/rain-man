package com.royvandewater.rainman.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Address;
import android.location.Location;
import com.royvandewater.rainman.util.NetworkFunctions;
import com.royvandewater.rainman.util.StringFunctions;

public class AddressParser
{
    private static final String GEOLOCATION_URL = "http://maps.google.com/maps/geo?ll=";
    public final static Locale DEFAULT_LOCALE = new Locale("en");

    public static Address retrieveForLocation(Location location) throws IOException, JSONException
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String uri = GEOLOCATION_URL + latitude + "," + longitude;
        
        InputStream geoStream = NetworkFunctions.executeHttpGet(uri);
        return AddressParser.parse(geoStream);
    }
    

    public static Address parse(InputStream sampleJsonInputStream) throws IOException, JSONException
    {
        Address address = new Address(DEFAULT_LOCALE);
        
        String jsonString = StringFunctions.streamToString(sampleJsonInputStream);
        JSONObject json = new JSONObject(jsonString);
        
        JSONObject addressDetails = json.getJSONArray("Placemark").getJSONObject(0).getJSONObject("AddressDetails");
        JSONObject locality = addressDetails.getJSONObject("Country").getJSONObject("AdministrativeArea").getJSONObject("Locality");

        address.setPostalCode(locality.getJSONObject("PostalCode").getString("PostalCodeNumber"));
        
        return address;
    }

}
