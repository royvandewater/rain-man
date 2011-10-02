package com.royvandewater.rainman.models;

import java.util.Date;
import java.util.HashMap;

public class Forecast {

	private String location;
	private Date date;
	private String weatherCondition;
	private int temperature;
	private double humidity;
	private HashMap<String, FutureCondition> futureConditions;
	
	public Forecast() {
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

	public String getLocation() {
		return location;
	}

	public Date getDate() {
		return date;
	}

	public String getWeatherCondition() {
		return this.weatherCondition;
	}

	public int getTemperature() {
		return this.temperature;
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
