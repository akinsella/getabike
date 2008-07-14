package org.helyx.app.j2me.lib.math;

public class MathUtil {
	
	private static final String CAT = "MATH_UTIL";

	private MathUtil() {
		super();
	}

	public static long power(long value, long power) {

		if (power == 0) {
			return 1;
		}

		if (power == 1) {
			return value;
		}
		
		long result = value;
		
		for (int i = 1 ; i < power ; i++) {
			result *= value;
		}

		return result;
	}
	
}
