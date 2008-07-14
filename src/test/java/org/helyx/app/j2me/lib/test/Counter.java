package org.helyx.app.j2me.lib.test;

public class Counter {

	private int counter = 0;
	
	public void inc() {
		counter++;
	}
	
	public void dec() {
		counter--;
	}
	
	public int getValue() {
		return counter;
	}
	
}
