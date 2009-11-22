package org.helyx.app.j2me.getabike.util;

import org.helyx.logging4me.Logger;

public class LocationUtil {
	
	private static final Logger logger = Logger.getLogger(LocationUtil.class);

	private LocationUtil() {
		super();
	}
	
	public static boolean supportLocationApi() {
	    if (System.getProperty("microedition.location.version") == null) {
	    	logger.info("No Location API detected on deviced");
	    	return false;
	    }
	    else {
	    	logger.info("Location API detected on deviced");
	    	return true;
	    }
	}

	
}
