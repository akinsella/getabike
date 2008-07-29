package org.helyx.app.j2me.lib.util;

import org.helyx.app.j2me.lib.constant.BooleanConstants;

public class BooleanUtil {

	private static final String CAT = "BOOLEAN_UTIL";
	
	private BooleanUtil() {
		super();
	}
	
	public static boolean getBoolean(String systemProperty) {
		String systemPropertyValue = System.getProperty(systemProperty);
		
		return BooleanConstants.TRUE.equals(systemPropertyValue);
	}
	
}
