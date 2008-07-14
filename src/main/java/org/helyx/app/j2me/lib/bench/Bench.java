package org.helyx.app.j2me.lib.bench;

public class Bench {

	long start;
	
	private Bench() {
		super();
		start = System.currentTimeMillis();
	}
	
	public static Bench start() {
		return new Bench();
	}
	
	public long getStart() {
		return start;
	}
	
	public long mesure() {
		return Math.max(0, System.currentTimeMillis() - start);
	}
	
}
