package org.helyx.app.j2me.lib.test;


import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.date.DateUtil;

public class DateUtilTest extends TestCase {

	public void testDate() {
		
		int year = 2008;
		int month = 1;
		int day = 1;
		int hour = 23;
		int minute = 10;
		int second = 21;
		int millis = 548;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millis);
		
		Date date = calendar.getTime();

		assertEquals(date, DateUtil.getDate(year, month, day, hour, minute, second, millis));

	}
	
}
