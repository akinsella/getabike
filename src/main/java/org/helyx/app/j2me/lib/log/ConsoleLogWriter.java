package org.helyx.app.j2me.lib.log;

import java.util.Date;

public class ConsoleLogWriter extends AbstractLogWriter {
	
	private static final Log log = LogFactory.getLog("CONSOLE_LOG_WRITER");
	
	private static ConsoleLogWriter consoleLogWriter;
	
	static {
		consoleLogWriter = new ConsoleLogWriter();
	}

	private ConsoleLogWriter() {
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

	public static ConsoleLogWriter getInstance() {
		return consoleLogWriter;
	}

}
