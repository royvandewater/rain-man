package com.royvandewater.rainman.models;

public class FutureCondition {

	public String DEFAULT_DAY = "Sun";
	public int DEFAULT_LOW = 0;
	public int DEFAULT_HIGH = 0;
	public String DEFAULT_CONDITION = "None";
	
	private String day;
	private int low;
	private int high;
	private String condition;

	public FutureCondition() {
		this.day = DEFAULT_DAY;
		this.low = DEFAULT_LOW;
		this.high = DEFAULT_HIGH;
		this.condition = DEFAULT_CONDITION;
	}
	
	public FutureCondition(String day, int low, int high, String condition) {
		this.day = day;
		this.low = low;
		this.high = high;
		this.condition = condition;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
