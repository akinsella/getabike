package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.format.NumberFormatUtil;

public class NumberFormatUtilTest extends TestCase {

	public void testNumberFormatOneDigit() {
		assertEquals("09", NumberFormatUtil.format(9, 2));
	}

	public void testNumberFormatTwoDigit() {
		assertEquals("10", NumberFormatUtil.format(10, 2));
	}

	public void testNumberFormatZeroValue() {
		assertEquals("00", NumberFormatUtil.format(0, 2));
	}
	
}
