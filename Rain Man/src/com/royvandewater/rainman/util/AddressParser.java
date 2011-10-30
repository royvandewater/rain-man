package com.royvandewater.rainman.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import com.royvandewater.rainman.models.Address;

public class AddressParser
{
    public final static Locale DEFAULT_LOCALE = new Locale("en");

    public Address parse(InputStream sampleJsonInputStream) throws IOException, JSONException
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
