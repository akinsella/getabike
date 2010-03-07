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

import org.helyx.logging4me.util.MathUtil;

public class NumberFormatUtil {

	private static final String ZERO = "0";
	
	private NumberFormatUtil() {
		super();
	}
	
	public static String format(long value, int digits) {

		if (digits < 2) {
			return String.valueOf(value);
		}

		if (value == 0) {
			StringBuffer sb = new StringBuffer();
			for (long i = 1 ; i < digits ; i++) {
				sb.append(ZERO);
			}
			sb.append(value);
			String result = sb.toString();
			
			return result;
		}
		
		long maxValue = MathUtil.pow((long)10, (long)digits);
		
		if (value >= maxValue) {
			String result = String.valueOf(value);
			
			return result;
		}
		
		StringBuffer sb = new StringBuffer();
		for (long i = value * 10 ; i < maxValue ; i *= 10) {
			sb.append(ZERO);
		}
		sb.append(value);
		String result = sb.toString();
		
		return result;
	}
	
}
