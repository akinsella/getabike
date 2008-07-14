package org.helyx.app.j2me.lib.log;

import java.util.Date;

import org.helyx.app.j2me.lib.format.DateFormatUtil;

public class ConsoleLogWriter implements ILogWriter {

	public ConsoleLogWriter() {
		super();
	}

	public void write(int level, String category, String message, Date date) {
		try {
		
			String dateStr = DateFormatUtil.formatDate(date);
			
			StringBuffer sb = new StringBuffer().append("[").append(Thread.currentThread().getName().toUpperCase()).append(" | ").append(Log.getLevelName(level)).append(" | ").append(category).append(" | ").append(dateStr).append("] ").append(message);
			
			if (level == Log.FATAL || level == Log.ERROR) {
				System.err.println(sb.toString());
			}
			else {
				System.out.println(sb.toString());
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

}
