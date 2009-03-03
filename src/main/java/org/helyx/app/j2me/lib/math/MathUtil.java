package org.helyx.app.j2me.lib.math;

import org.helyx.app.j2me.lib.dfp.dfp;
import org.helyx.app.j2me.lib.dfp.dfpmath;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class MathUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("MATH_UTIL");

	private MathUtil() {
		super();
	}
	
	public static double round(double num) {
		double floor = Math.floor(num);
		
		if(num - floor >= 0.5) {
			return Math.ceil(num);
		}
		else {
			return floor;
		}
	}

	public static double exp(double value) {
		dfp exp = dfpmath.exp(new dfp(String.valueOf(value)));
		double result = Double.parseDouble(exp.toString());
		return result;
	}

	public static double pow(double value1, double value2) {
		dfp pow = dfpmath.pow(new dfp(String.valueOf(value1)), new dfp(String.valueOf(value2)));
		double result = Double.parseDouble(pow.toString());
		return result;
	}

	public static double pow(double value1, int value2) {
		dfp pow = dfpmath.pow(new dfp(String.valueOf(value1)), value2);
		double result = Double.parseDouble(pow.toString());
		return result;
	}


	public static long pow(int value, int power) {
		 return pow((long)value, (long)power);
	}
	
	public static long pow(long value, long power) {

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

	public static double acos(double value) {
		dfp atan = dfpmath.acos(new dfp(String.valueOf(value)));
		double result = Double.parseDouble(atan.toString());
		return result;
	}

	public static double asin(double value) {
		dfp atan = dfpmath.asin(new dfp(String.valueOf(value)));
		double result = Double.parseDouble(atan.toString());
		return result;
	}

	public static double atan(double value) {
		dfp atan = dfpmath.atan(new dfp(String.valueOf(value)));
		double result = Double.parseDouble(atan.toString());
		return result;
	}
	
	public static double ln(double value) {
		dfp logger = dfpmath.ln(new dfp( String.valueOf(value)));
		double result = Double.parseDouble(logger.toString());
		return result;
	}
	
}