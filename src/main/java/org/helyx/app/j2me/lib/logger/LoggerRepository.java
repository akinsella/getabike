package org.helyx.app.j2me.lib.logger;

import java.util.Hashtable;

public class LoggerRepository {
	
	private static Hashtable loggerMap = new Hashtable();

	public LoggerRepository() {
		super();
	}
	
	public static Logger getLogger(String category) {
		if (!loggerMap.containsKey(category)) {
			loggerMap.put(category, new Logger(category));
		}
		
		Logger logger = (Logger)loggerMap.get(category);
		
		return logger;
	}

}
