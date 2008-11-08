package org.helyx.app.j2me.lib.format;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.MathUtil;

public class NumberFormatUtil {
	
	private static final Log log = LogFactory.getLog("NUMBER_FORMAT_UTIL");

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
				sb.append("0");
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
			sb.append("0");
		}
		sb.append(value);
		String result = sb.toString();
		
		return result;
	}
	
}
