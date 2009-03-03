package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.math.MathUtil;

public class MathUtilTest extends TestCase {
	
	private static final Logger logger = LoggerFactory.getLogger("MATH_UTIL_TEST");

	public void testPower() {
		assertEquals(1, MathUtil.pow((long)10, (long)0));
		assertEquals(10, MathUtil.pow((long)10, (long)1));
		assertEquals(100, MathUtil.pow((long)10, (long)2));
	}
	
}
