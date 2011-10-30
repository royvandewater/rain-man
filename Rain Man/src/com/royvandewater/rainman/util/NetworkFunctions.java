package com.royvandewater.rainman.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class NetworkFunctions
{
    
    public static InputStream executeHttpGet(String uri) throws IOException, ClientProtocolException
    {
        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(uri));
        return httpResponse.getEntity().getContent();
    }
}
