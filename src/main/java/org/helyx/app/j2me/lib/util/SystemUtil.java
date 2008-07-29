package org.helyx.app.j2me.lib.util;

public class SystemUtil {

	private SystemUtil() {
		super();
	}
	
	public static String getProperty(String systemProperty, String defaultValue) {
		String systemPropertyValue = System.getProperty(systemProperty);
		if (systemPropertyValue == null) {
			return defaultValue;
		}
		
		return systemPropertyValue;
	}
	
}
