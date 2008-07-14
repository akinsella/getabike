package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.math.MathUtil;

public class MathUtilTest extends TestCase {

	public void testPower() {
		assertEquals(1, MathUtil.power(10, 0));
		assertEquals(10, MathUtil.power(10, 1));
		assertEquals(100, MathUtil.power(10, 2));
	}
	
}
