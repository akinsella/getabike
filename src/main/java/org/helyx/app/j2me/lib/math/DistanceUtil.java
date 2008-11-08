package org.helyx.app.j2me.lib.math;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class DistanceUtil {
	
	private static final Log log = LogFactory.getLog("DISTANCE_UTIL");

	private static int offset = 268435456;
	private static double radius = offset / Math.PI;

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
	  dist = MathUtil.acos(dist);
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

	public static double LToX(double x) {
		return offset + radius * x * Math.PI / 180;
	}
	 
	public static double XToL(double x) {
		return ((x - offset) / radius) * 180 / Math.PI;
	}
	 
	public static double LToY(double y) {
		double tmp = Math.sin(y * Math.PI / 180);
		return offset - radius * MathUtil.ln((1 + tmp) / (1 - tmp)) / 2;
	}
	
	public static double YToL(double y) {
		return (Math.PI / 2 - 2 * MathUtil.atan(MathUtil.exp((y - offset) / radius))) * 180 / Math.PI;
	}

}
