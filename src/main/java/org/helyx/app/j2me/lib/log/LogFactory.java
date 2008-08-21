package org.helyx.app.j2me.lib.log;

import java.util.Hashtable;

public class LogFactory {
	
	private static Hashtable loggerMap = new Hashtable();

	private LogFactory() {
		super();
	}
	
	public static Log getLog(String cat) {
		if (!loggerMap.containsKey(cat)) {
			loggerMap.put(cat, new Log(cat));
		}
		
		Log log = (Log)loggerMap.get(cat);
		
		return log;
	}

}
