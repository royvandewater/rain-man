package com.royvandewater.rainman.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParserException;

import com.royvandewater.rainman.models.Forecast;

public class GoogleForecastParser extends DefaultHandler {

	
	static final String CITY = "city";
	static final String CURRENT_DATE_TIME = "current_date_time";
	static final String CURRENT_CONDITION = "current_conditions";
	static final String FORECAST_CONDITIONS = "forecast_conditions";
	static final String CONDITION = "condition";
	static final String HUMIDITY = "humidity";
	static final String DAY_OF_WEEK = "day_of_week";
	static final String LOW = "low";
	static final String HIGH = "high";
	
	private Forecast forecast;
	private StringBuilder builder;
	
	public Forecast parse (InputStream inputStream) throws XmlPullParserException, ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
		
		return new Forecast();
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (this.forecast != null) {
			if (localName.equalsIgnoreCase(CITY)) {
				forecast.setLocation(builder.toString());
			}
			
			builder.setLength(0);
		}
	}
}
