package com.royvandewater.rainman.test.parsers;

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
import com.royvandewater.rainman.parsers.GoogleForecastParser;
import com.royvandewater.rainman.util.StringFunctions;

public class GoogleForecastParserTest extends InstrumentationTestCase {

    final private String CLASS_NAME = "class com.royvandewater.rainman.parsers.GoogleForecastParser";
    final private String FIXTURE = "google_forecast.xml";

    private static final String EXPECTED_LOCATION = "Mountain View, CA";
    private static final Date EXPECTED_DATE = new Date(1315889760000L);
    private static final double EXPECTED_HUMIDITY = 0.73;
    private static final String EXPECTED_WEATHER_CONDITION = "Clear";

    private InputStream sampleXmlInputStream;
    private GoogleForecastParser forecastParser;

    @Override
    protected void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
        this.sampleXmlInputStream = assetManager.open(FIXTURE);
        this.forecastParser = new GoogleForecastParser();
    }
    
    @Override
    protected void tearDown() throws Exception {
    	sampleXmlInputStream.close();
    }

    public void testConstructor() {
        assertEquals(CLASS_NAME, forecastParser.getClass().toString());
    }

    public void testFixture() throws IOException {
        String sampleXml = StringFunctions.streamToString(sampleXmlInputStream);
        assertTrue(sampleXml.startsWith("<xml_api_reply version=\"1\">"));
    }

    public void testParseDateMethod() throws ParseException {
        Date date = forecastParser.parseDate("2011-09-13 04:56:00 +0000");
        assertEquals(EXPECTED_DATE, date);
    }
    
    public void testParseHumidityMethod() {
        double humidity = forecastParser.parseHumidity("Humidity: 73%");
        assertEquals(EXPECTED_HUMIDITY, humidity);
    }

    public void testParseDate() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
        Forecast forecast = forecastParser.parse(sampleXmlInputStream);
        assertEquals(EXPECTED_DATE, forecast.getDate());
    }

    public void testParseLocation() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
        Forecast forecast = forecastParser.parse(sampleXmlInputStream);
        assertEquals(EXPECTED_LOCATION, forecast.getLocation());
    }

    public void testParseWeatherCondition() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
        Forecast forecast = forecastParser.parse(sampleXmlInputStream);
        assertEquals(EXPECTED_WEATHER_CONDITION, forecast.getWeatherCondition());
    }
    
    public void testParseHumidity() throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
        Forecast forecast = forecastParser.parse(sampleXmlInputStream);
        assertEquals(EXPECTED_HUMIDITY, forecast.getHumidity());
    }
}
