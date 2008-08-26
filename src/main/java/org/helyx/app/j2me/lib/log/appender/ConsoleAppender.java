package org.helyx.app.j2me.lib.log.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ConsoleAppender extends AbstractAppender {
	
	private static final Log log = LogFactory.getLog("CONSOLE_APPENDER");
	
	private static ConsoleAppender consoleLogWriter;
	
	static {
		consoleLogWriter = new ConsoleAppender();
	}

	private ConsoleAppender() {
		super();
	}

	public void onWrite(int level, Log log, String message, Date date) {
		try {			
			if (level == Log.FATAL || level == Log.ERROR) {
				System.err.println(getLogMessage(level, log, message, date));
			}
			else {
				System.out.println(getLogMessage(level, log, message, date));
			}
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public void open() throws Exception {

	}

	public void close() throws Exception {
		
	}

	public void flush() throws Exception {
		
	}

	public static ConsoleAppender getInstance() {
		return consoleLogWriter;
	}

}
