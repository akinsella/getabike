package org.helyx.app.j2me.lib.math;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class MathApprox {
	
	private static final Logger logger = LoggerFactory.getLogger("MATH_APPROX");

	private MathApprox() {
		super();
	}

	public static double ln(double val) {  
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
	
	public static double pow(final double a, final double b) {
	    final int x = (int) (Double.doubleToLongBits(a) >> 32);
	    final int y = (int) (b * (x - 1072632447) + 1072632447);
	    return Double.longBitsToDouble(((long) y) << 32);
	}

	/**
	 * 	arccos(y) = arctan(sqrt(1-y²)/y) 
	 *  arccos(y) is approximately sqrt(1-y)*(1.5707288-0.212114*y+0.0742610*y²-0.0187293*y³)
	 */
	public static double acos(double y) {
		return atan(Math.sqrt(1 - (y * y)) / y);
	}
	
    /**
     * For the inverse tangent calls, all approximations are valid for |t| <= 1.
     * To compute ATAN(t) for t > 1, use ATAN(t) = PI/2 - ATAN(1/t).  For t < -1,
     * use ATAN(t) = -PI/2 - ATAN(1/t).
     */

	/**
	 * arctan(y) is approximately y/(1+.28y²) for -1<=y<=1
	 * and arctan(y) = pi/2 - arctan(1/y) for y < -1 or y > 1
	 * @param y
	 * @return
	 */
	public static double atan(double y) {
		if (-1 <= y && y <= 1) {
			return (y + 0.43157974*y*y*y)/(1 + 0.76443945*y*y + 0.05831938*y*y*y*y);
//			return y/(1 + 0.280872 * y * y);
		}
		else {
			return Math.PI / 2 - atan(1 / y);
		}
	}
	
}