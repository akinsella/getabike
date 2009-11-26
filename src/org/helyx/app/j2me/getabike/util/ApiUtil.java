package org.helyx.app.j2me.getabike.util;

import org.helyx.logging4me.Logger;

public class ApiUtil {
	
	private static final Logger logger = Logger.getLogger("API_UTIL");
	
	private static final String LOCATION_API_VERSION = "microedition.location.version";

	private static final String PIM_API_VERSION = "microedition.pim.version";

	private static final String FILE_API_VERSION = "microedition.io.file.FileConnection.version";

	private ApiUtil() {
		super();
	}
	
	public static boolean supportLocationApi() {
	    if (getLocationApiVersion() == null) {
	    	logger.info("No Location API detected on deviced");
	    	return false;
	    }
	    else {
	    	logger.info("Location API detected on deviced");
	    	return true;
	    }
	}
	
	public static String getLocationApiVersion() {
		return System.getProperty(LOCATION_API_VERSION);
	}
	
	public static boolean supportPimApi() {
		
	    if (getPimApiVersion() == null) {
	    	logger.info("No PIM API detected on deviced");
	    	return false;
	    }
	    else {
	    	logger.info("PIM API detected on deviced");
	    	return true;
	    }
	}
	
	public static String getPimApiVersion() {
		return System.getProperty(PIM_API_VERSION);
	}

	public static boolean supportFileApi() {
		
	    if (getFileApiVersion() == null) {
	    	logger.info("No PIM API detected on deviced");
	    	return false;
	    }
	    else {
	    	logger.info("PIM API detected on deviced");
	    	return true;
	    }
	}
	
	public static String getFileApiVersion() {
		return System.getProperty(FILE_API_VERSION);
	}
	
}
