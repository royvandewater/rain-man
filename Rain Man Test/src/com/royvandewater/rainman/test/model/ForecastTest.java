package com.royvandewater.rainman.test.model;

import java.util.Date;

import junit.framework.TestCase;

import com.royvandewater.rainman.models.Forecast;
import com.royvandewater.rainman.models.FutureCondition;

public class ForecastTest extends TestCase {

	private static final String LOCATION = "Mountain View, CA";
	private static final Date DATE = new Date();
	private static final String WEATHER_CONDITION = "Overcast";
	private static final int TEMPERATURE = 60;
	private static final double HUMIDITY = 0.80;
	
	private Forecast forecast;

	@Override
	protected void setUp() throws Exception {
		this.forecast = new Forecast(LOCATION, DATE, WEATHER_CONDITION, TEMPERATURE, HUMIDITY);
	}
	
	public void testConstructor() {
		assertEquals(LOCATION, forecast.getLocation());
		assertEquals(DATE, forecast.getDate());
		assertEquals(WEATHER_CONDITION, forecast.getWeatherCondition());
		assertEquals(TEMPERATURE, forecast.getTemperature());
		assertEquals(HUMIDITY, forecast.getHumidity());
	}
	
	public void testFutureConditions() {
		FutureCondition mondayCondition = new FutureCondition();
		FutureCondition tuesdayCondition = new FutureCondition();
		mondayCondition.setDay("Mon");
		tuesdayCondition.setDay("Tue");
		
		forecast.addFutureCondition(mondayCondition);
		forecast.addFutureCondition(tuesdayCondition);
		
		assertEquals(mondayCondition, forecast.getFutureCondition("Mon"));
		assertEquals(tuesdayCondition, forecast.getFutureCondition("Tue"));
	}
}
