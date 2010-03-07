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
package org.helyx.app.j2me.getabike.lib.format;

import java.util.Calendar;
import java.util.Date;

import org.helyx.app.j2me.getabike.lib.format.NumberFormatUtil;

public class DateFormatUtil {
	
	private DateFormatUtil() {
		super();
	}

	public static String formatDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String year = NumberFormatUtil.format(calendar.get(Calendar.YEAR), 4);
		String month = NumberFormatUtil.format(calendar.get(Calendar.MONTH) + 1, 2);
		String day = NumberFormatUtil.format(calendar.get(Calendar.DATE), 2);
		String hour = NumberFormatUtil.format(calendar.get(Calendar.HOUR_OF_DAY), 2);
		String minute = NumberFormatUtil.format(calendar.get(Calendar.MINUTE), 2);
		String second = NumberFormatUtil.format(calendar.get(Calendar.SECOND), 2);
		
		return day + "/" + month + "/" + year + ", " + hour + ":" + minute + ":" + second;
	}
	
}
