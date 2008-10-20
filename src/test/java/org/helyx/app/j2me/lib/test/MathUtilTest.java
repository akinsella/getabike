package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.MathUtil;

public class MathUtilTest extends TestCase {
	
	private static final Log log = LogFactory.getLog("MATH_UTIL_TEST");

	public void testPower() {
		assertEquals(1, MathUtil.power(10, 0));
		assertEquals(10, MathUtil.power(10, 1));
		assertEquals(100, MathUtil.power(10, 2));
	}
	
	public void testDistance() {
		System.out.println(MathUtil.distance(47.2471819477, 6.02276834043, 47.2328969915, 6.0203034105, MathUtil.KM) * 1000);
	}
	
}
