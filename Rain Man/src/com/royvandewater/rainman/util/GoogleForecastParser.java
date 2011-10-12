package com.royvandewater.rainman.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;
import com.royvandewater.rainman.models.Forecast;

public class GoogleForecastParser extends DefaultHandler {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    public static String DATA = "data";

    public enum Tag {
        /* Sections */
        FORECAST_INFORMATION, CURRENT_CONDITIONS, FORECAST_CONDITIONS,
        /* Tags */
        WEATHER, CITY, CURRENT_DATE_TIME, CONDITION, HUMIDITY, DAY_OF_WEEK, LOW, HIGH,

        NOVALUE;

        public static Tag toTag(String str) {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    private Forecast forecast;
    private StringBuilder builder;

    private boolean inForecastInformation, inCurrentConditions, inForecastConditions;

    public Forecast parse(InputStream inputStream) throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(inputStream, this);

        return forecast;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        this.builder = new StringBuilder();
        this.forecast = new Forecast();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (inCurrentConditions) {
            switch (Tag.toTag(localName)) {
                case CONDITION:
                    forecast.setWeatherCondition(attributes.getValue(DATA));
                    break;
                case HUMIDITY:
                    double humidity = parseHumidity(attributes.getValue(DATA));
                    forecast.setHumidity(humidity);
            }
        } else {
            switch (Tag.toTag(localName)) {
                case CITY:
                    forecast.setLocation(attributes.getValue(DATA));
                    break;
                case CURRENT_DATE_TIME:
                    Date date = parseDate(attributes.getValue(DATA));
                    forecast.setDate(date);
                    break;
                case FORECAST_INFORMATION:
                    inForecastInformation = true;
                    break;
                case CURRENT_CONDITIONS:
                    inCurrentConditions = true;
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        switch (Tag.toTag(localName)) {
            case FORECAST_INFORMATION:
                inForecastInformation = false;
                break;
            case CURRENT_CONDITIONS:
                inCurrentConditions = false;
                break;
        }

        builder.setLength(0);
    }

    public Date parseDate(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public double parseHumidity(String string) {
        String humidityString = "." + string.substring(10, 12);
        return Double.parseDouble(humidityString);
    }
}
