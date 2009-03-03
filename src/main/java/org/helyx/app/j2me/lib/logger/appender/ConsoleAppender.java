package org.helyx.app.j2me.lib.logger.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class ConsoleAppender extends AbstractAppender {
	
	private static final Logger logger = LoggerFactory.getLogger("CONSOLE_APPENDER");
	
	private static ConsoleAppender consoleLogWriter;
	
	public static String CONSOLE_APPENDER_NAME = "CONSOLE";
	
	static {
		consoleLogWriter = new ConsoleAppender();
	}

	private ConsoleAppender() {
		super();
	}

	public void onWrite(int level, Logger logger, String message, Date date) {
		try {			
			if (level == Logger.FATAL || level == Logger.ERROR) {
				System.err.println(getLogMessage(level, logger, message, date));
			}
			else {
				System.out.println(getLogMessage(level, logger, message, date));
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

	public String getName() {
		return CONSOLE_APPENDER_NAME;
	}

}
