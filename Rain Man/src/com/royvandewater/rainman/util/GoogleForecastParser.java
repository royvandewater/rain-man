package com.royvandewater.rainman.util;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.royvandewater.rainman.models.Forecast;

public class GoogleForecastParser {
	

	public Forecast parse (String xml) throws XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		
		return new Forecast();
	}
}
