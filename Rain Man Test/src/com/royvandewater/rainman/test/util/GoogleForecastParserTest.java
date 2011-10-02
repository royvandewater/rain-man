package com.royvandewater.rainman.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;

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
	
	private static final String EXPECTED_LOCATION = "Mountain View, CA";
	private static final Date EXPECTED_DATE = new Date(1315889760000L);
	
	private InputStream sampleXmlInputStream;
	private GoogleForecastParser forecastParser;
	
	@Override
	protected void setUp() throws Exception {
		Context context = getInstrumentation().getContext();
		AssetManager assetManager = context.getAssets();
		this.sampleXmlInputStream = assetManager.open(FIXTURE);
		this.forecastParser = new GoogleForecastParser();
	}
	
	public void testConstructor() {
		assertEquals(CLASS_NAME, forecastParser.getClass().toString());
	}
	
	public void testFixture() throws IOException {
		String sampleXml = StringFunctions.streamToString(sampleXmlInputStream);
		assertTrue(sampleXml.startsWith("<xml_api_reply version=\"1\">"));
	}
	
	public void testParseDate() throws ParseException {
		Date date = forecastParser.parseDate("2011-09-13 04:56:00 +0000");
		assertEquals(EXPECTED_DATE, date);
	}
	
	public void testParse() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
		
		Forecast forecast = forecastParser.parse(sampleXmlInputStream);
		
		assertEquals(EXPECTED_LOCATION, forecast.getLocation());
		assertEquals(EXPECTED_DATE, forecast.getDate());
	}
}
