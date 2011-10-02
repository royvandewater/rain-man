package com.royvandewater.rainman.test.model;

import junit.framework.TestCase;

import com.royvandewater.rainman.models.FutureCondition;

public class ConditionTest extends TestCase {

	final static String DAY_OF_WEEK = "Sun";
	final static int LOW = 52;
	final static int HIGH = 72;
	final static String CONDITION = "Overcast";
	
	private FutureCondition condition;
	
	@Override
	protected void setUp() throws Exception {
		this.condition = new FutureCondition(); 
	}

	public void testDefaultConstructor() {
		assertEquals("Sun", condition.getDay());
		assertEquals(0, condition.getLow());
		assertEquals(0, condition.getHigh());
		assertEquals("None", condition.getCondition());
	}
	
	public void testConstructor() {
		FutureCondition condition = new FutureCondition(DAY_OF_WEEK, LOW, HIGH, CONDITION);
		assertEquals(DAY_OF_WEEK, condition.getDay());
		assertEquals(LOW, condition.getLow());
		assertEquals(HIGH, condition.getHigh());
		assertEquals(CONDITION, condition.getCondition());
	}
	
	public void testSetDay() {
		condition.setDay(DAY_OF_WEEK);
		assertEquals(DAY_OF_WEEK, condition.getDay());
	}
	
	public void testSetLow() {
		condition.setLow(LOW);
		assertEquals(LOW, condition.getLow());
	}
	
	public void testSetHigh() {
		condition.setHigh(HIGH);
		assertEquals(HIGH, condition.getHigh());
	}
	
	public void testSetCondition() {
		condition.setCondition(CONDITION);
		assertEquals(CONDITION, condition.getCondition());
	}
}
