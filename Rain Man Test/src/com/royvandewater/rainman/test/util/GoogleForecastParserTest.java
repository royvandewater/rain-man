package com.royvandewater.rainman.test.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
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
	private InputStream sampleXmlInputStream;
	
	@Override
	protected void setUp() throws Exception {
		Context context = getInstrumentation().getContext();
		AssetManager assetManager = context.getAssets();
		this.sampleXmlInputStream = assetManager.open(FIXTURE);
	}
	
	public void testConstructor() {
		GoogleForecastParser forecastParser = new GoogleForecastParser();
		assertEquals(CLASS_NAME, forecastParser.getClass().toString());
	}
	
	public void testFixture() throws IOException {
		String sampleXml = StringFunctions.streamToString(sampleXmlInputStream);
		assertTrue(sampleXml.startsWith("<xml_api_reply version=\"1\">"));
	}
	
	public void testParse() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
		GoogleForecastParser forecastParser = new GoogleForecastParser();
		Forecast forecast = forecastParser.parse(sampleXmlInputStream);
		assertEquals("Mountain View, CA", forecast.getLocation());
	}
}
