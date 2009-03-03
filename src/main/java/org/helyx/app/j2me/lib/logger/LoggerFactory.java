package org.helyx.app.j2me.lib.logger;

import java.util.Hashtable;

public class LoggerFactory {
	
	private static LoggerRepository loggerRepository = new LoggerRepository();

	private LoggerFactory() {
		super();
	}
	
	public static Logger getLogger(String category) {
		Logger logger = loggerRepository.getLogger(category);
		
		return logger;
	}

}
