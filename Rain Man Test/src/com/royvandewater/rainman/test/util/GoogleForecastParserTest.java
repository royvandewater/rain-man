package com.royvandewater.rainman.test.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.util.GoogleForecastParser;
import com.royvandewater.rainman.util.StringFunctions;

public class GoogleForecastParserTest extends InstrumentationTestCase {

	final private String CLASS_NAME = "class com.royvandewater.rainman.util.GoogleForecastParser";
	final private String FIXTURE    = "google_forecast.xml";
	private String sampleXml;
	
	@Override
	protected void setUp() throws Exception {
		Context context = getInstrumentation().getContext();
		AssetManager assetManager = context.getAssets();
		InputStream sampleXmlInputStream = assetManager.open(FIXTURE);
		sampleXml = StringFunctions.streamToString(sampleXmlInputStream);
		
	}
	
	public void testConstructor() {
		GoogleForecastParser forecastParser = new GoogleForecastParser();
		assertEquals(CLASS_NAME, forecastParser.getClass().toString());
	}
	
	public void testFixture() {
		assertTrue(sampleXml.startsWith("<xml_api_reply version=\"1\">"));
	}
	
	public void testParse() throws XmlPullParserException {
		GoogleForecastParser forecastParser = new GoogleForecastParser();
		Forecast forecast = forecastParser.parse(sampleXml);
	}
}
