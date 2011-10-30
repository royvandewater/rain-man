package com.royvandewater.rainman.models;

import java.util.Date;
import java.util.HashMap;

public class Forecast {

	public static final String DEFAULT_LOCATION = "None";
	public static final int DEFAULT_TEMPERATURE = 0;
	public static final double DEFAULT_HUMIDITY = 0.0;
	public static final String DEFAULT_WEATHER_CONDITION = "None";
	
	private String location;
	private Date date;
	private String weatherCondition;
	private int temperature;
	private double humidity;
	private HashMap<String, FutureCondition> futureConditions;
	
	public Forecast() {
		this.location = DEFAULT_LOCATION;
		this.temperature = DEFAULT_TEMPERATURE;
		this.humidity = DEFAULT_HUMIDITY;
		this.weatherCondition = DEFAULT_WEATHER_CONDITION;
		this.futureConditions = new HashMap<String, FutureCondition>();
	}
	
	public Forecast(String location, Date date, String weatherCondition, int temperature, double humidity) {
		this();
		this.location = location;
		this.date = date;
		this.weatherCondition = weatherCondition;
		this.temperature = temperature;
		this.humidity = humidity;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	
	public String getWeatherCondition() {
		return this.weatherCondition;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public int getTemperature() {
		return this.temperature;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	
	public double getHumidity() {
		return this.humidity;
	}

	public void addFutureCondition(FutureCondition condition) {
		futureConditions.put(condition.getDay(), condition);
	}

	public FutureCondition getFutureCondition(String day) {
		return futureConditions.get(day);
	}
}
