/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.math;

import org.helyx.logging4me.Logger;


public class MathApprox {
	
	private static final Logger logger = Logger.getLogger("MATH_APPROX");

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