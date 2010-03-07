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
package org.helyx.app.j2me.getabike.lib.test;


import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.lib.date.DateUtil;

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
