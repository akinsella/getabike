package org.helyx.app.j2me.lib.math;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class MathUtil {
	
	private static final Log log = LogFactory.getLog("MATH_UTIL");

	private MathUtil() {
		super();
	}
	
	public double ln(double val) {  
	    final double x = (Double.doubleToLongBits(val) >> 32);  
	    return (x - 1072632447) / 1512775;  
	}
	
	public static double log(double x) {  
		return 6 * (x - 1) / (x + 1 + 4 * (Math.sqrt(x)));  
	} 
	
	public static double exp(double val) {  
		final long tmp = (long) (1512775 * val + (1072693248 - 60801));  
		return Double.longBitsToDouble(tmp << 32);  
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
	
	public static double pow(final double a, final double b) {
	    final int x = (int) (Double.doubleToLongBits(a) >> 32);
	    final int y = (int) (b * (x - 1072632447) + 1072632447);
	    return Double.longBitsToDouble(((long) y) << 32);
	}

	/**
	 * 	arccos(y) = arctan(sqrt(1-y²)/y) 
	 *  arccos(y) is approximately sqrt(1-y)*(1.5707288-0.212114*y+0.0742610*y²-0.0187293*y³)
	 */
	public static double arccos(double y) {
		return arctan(Math.sqrt(1 - (y * y)) / y);
	}
	
	/**
	 * arctan(y) is approximately y/(1+.28y²) for -1<=y<=1
	 * and arctan(y) = pi/2 - arctan(1/y) for y < -1 or y > 1
	 * @param y
	 * @return
	 */
	public static double arctan(double y) {
		if (-1 <= y && y <= 1) {
			return y/(1 + 0.28 * y * y);
		}
		else{
			double y2 = 1/y;
			return Math.PI / 2 - (y2 / (1 + 0.28 * y2 * y2));
		}
	}
	
	public static final String KM = "KM";
	public static final String MILES = "MILES";
	public static final String NAUT = "NAUT";
	
	/**
	 * 
	 * m' is statute miles
     * 'k' is kilometers (default)
     * 'n' is nautical miles   
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @param unit
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
	  double theta = lon1 - lon2;
	  double dist;
	  dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
	  dist = arccos(dist);
	  dist = Math.toDegrees(dist);
	  double distance = dist * 60 * 1.1515;
	  if (KM.equals(unit)) {
	    distance = distance * 1.609344;
	  }
	  else if (NAUT.equals(unit)) {
	      distance = distance * 0.8684;
	  }
	  return distance;
	}
	
}