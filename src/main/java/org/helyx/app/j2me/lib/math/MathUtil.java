package org.helyx.app.j2me.lib.math;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class MathUtil {
	
	private static final Log log = LogFactory.getLog("MATH_UTIL");

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
