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
		this.forecast = new Forecast();
	}
	
	public void testDefaultConstructor() {
		assertEquals("None", forecast.getLocation());
		assertEquals(0, forecast.getTemperature());
		assertEquals(0.0, forecast.getHumidity());
		assertEquals("None", forecast.getWeatherCondition());
	}
	
	public void testConstructor() {
		Forecast forecast = new Forecast(LOCATION, DATE, WEATHER_CONDITION, TEMPERATURE, HUMIDITY);
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
	
	public void testSetLocation() {
		forecast.setLocation(LOCATION);
		assertEquals(LOCATION, forecast.getLocation());
	}
	
	public void testSetDate() {
		forecast.setDate(DATE);
		assertEquals(DATE, forecast.getDate());
	}
	
	public void testSetWeatherCondition() {
		forecast.setWeatherCondition(WEATHER_CONDITION);
		assertEquals(WEATHER_CONDITION, forecast.getWeatherCondition());
	}
	
	public void testSetTemperature() {
		forecast.setTemperature(TEMPERATURE);
		assertEquals(TEMPERATURE, forecast.getTemperature());
	}
	
	public void testSetHumidity() {
		forecast.setHumidity(HUMIDITY);
		assertEquals(HUMIDITY, forecast.getHumidity());
	}
}
