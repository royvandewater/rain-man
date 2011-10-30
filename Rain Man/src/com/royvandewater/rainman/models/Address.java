package com.royvandewater.rainman.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import android.location.Location;
import com.royvandewater.rainman.util.AddressParser;

public class Address extends android.location.Address
{
    private static final String GEOLOCATION_URL = "http://maps.google.com/maps/geo?ll=";

    public Address(Locale locale)
    {
        super(locale);
    }
    
    public static Address buildFromLocation(Location location) throws IOException, JSONException
    {
        AddressParser addressParser = new AddressParser();
        
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String uri = GEOLOCATION_URL + latitude + "," + longitude;
        
        InputStream geoStream = getGeocodedInformation(uri);
        return addressParser.parse(geoStream);
    }
    
    private static InputStream getGeocodedInformation(String uri) throws IOException, ClientProtocolException
    {
        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(uri));
        return httpResponse.getEntity().getContent();
    }

}
