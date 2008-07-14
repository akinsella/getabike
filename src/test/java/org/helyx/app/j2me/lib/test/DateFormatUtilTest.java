package org.helyx.app.j2me.lib.test;


import java.util.Date;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.date.DateUtil;
import org.helyx.app.j2me.lib.format.DateFormatUtil;

public class DateFormatUtilTest extends TestCase {

	public void testDateFormat() {
		Date date = DateUtil.getDate(2008, 9, 1, 17, 24, 8);
		assertEquals("01/09/2008, 17:24:08", DateFormatUtil.formatDate(date));
	}

	public void testDateFormatWithZero() {
		Date date = DateUtil.getDate(2008, 9, 1, 17, 24, 0);
		assertEquals("01/09/2008, 17:24:00", DateFormatUtil.formatDate(date));
	}
	
}
