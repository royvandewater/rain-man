package com.royvandewater.rainman.test.parsers;

import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Address;
import android.test.InstrumentationTestCase;

import com.royvandewater.rainman.parsers.AddressParser;

public class AddressParserTest extends InstrumentationTestCase {
	
	private static final String EXPECTED_POSTAL_CODE = "85225";
	final private String FIXTURE = "location.json";
	private InputStream sampleJsonInputStream;
	
	@Override
	protected void setUp() throws Exception {
		Context context = getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
    	sampleJsonInputStream = assetManager.open(FIXTURE);
	}
	
	@Override
	protected void tearDown() throws Exception {
		sampleJsonInputStream.close();
	}
	
    public void testParseZipcode() throws IOException, JSONException {
    	Address address = AddressParser.parse(sampleJsonInputStream);
    	assertEquals(EXPECTED_POSTAL_CODE, address.getPostalCode());
    }

}
