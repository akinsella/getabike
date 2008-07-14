package org.helyx.app.j2me.lib.log;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public class Log {

	public static final int FATAL = 1;
	public static final int ERROR = 2;
	public static final int WARN = 3;
	public static final int INFO = 4;
	public static final int DEBUG = 5;
	public static final int TRACE = 6;

	public static final String FATAL_STR = "FATAL";
	public static final String ERROR_STR = "ERROR";
	public static final String WARN_STR = "WARN";
	public static final String INFO_STR = "INFO";
	public static final String DEBUG_STR = "DEBUG";
	public static final String TRACE_STR = "TRACE";

	private static final String DEFAULT = "DEFAULT";
	
	private static final String NULL_STR = "null";

	private static Vector logWriterList = new Vector();
	
	private static int thresholdLevel = DEBUG;
	
	static {
		logWriterList = new Vector();
		logWriterList.addElement(new ConsoleLogWriter());
	}

	public static String getLevelName(int level) {
		switch(level) {
			case Log.FATAL:
				return FATAL_STR;
			case Log.ERROR:
				return ERROR_STR;
			case Log.WARN:
				return WARN_STR;
			case Log.INFO:
				return INFO_STR;
			case Log.DEBUG:
				return DEBUG_STR;
			case Log.TRACE:
				return TRACE_STR;
			default:
				return INFO_STR;
		}
	}
	
	public synchronized static void addLogWriter(ILogWriter logWriter) {
		logWriterList.addElement(logWriter);
	}

	public synchronized static boolean existLogWriter(ILogWriter logWriter) {
		return logWriterList.contains(logWriter);
	}

	public synchronized static void removeLogWriter(ILogWriter logWriter) {
		logWriterList.removeElement(logWriter);
	}

	public synchronized static Vector getLogWriterList() {
		
		Vector newLogWriterList = new Vector();
	
		Enumeration _enum = logWriterList.elements();
		
		while (_enum.hasMoreElements()) {	
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			newLogWriterList.addElement(logWriter);
		}
		
		return newLogWriterList;
	}

	public synchronized static void flushLogWriters() throws Exception {
		Enumeration _enum = logWriterList.elements();
		
		while (_enum.hasMoreElements()) {			
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			logWriter.flush();
		}
	}

	public synchronized static void closeLogWriters() throws Exception {
		Enumeration _enum = getLogWriterList().elements();

		logWriterList.removeAllElements();

		while (_enum.hasMoreElements()) {			
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			logWriter.close();
		}
	}

	private static String getThrowableMessage(Throwable throwable) {
		return "Exception : " + throwable.getMessage() + " - " + throwable.getClass().getName();
	}

	private synchronized static void write(int level, String category, Object object) {
//		System.err.println("level: " + level + ", thresholdLevel: "  + thresholdLevel);
		if (level > Log.thresholdLevel) {
			return; 
		}
		
		if (object != null) {

			Date date = new Date();

			Enumeration _enum = logWriterList.elements();
			
			String message = null;
			if (object instanceof String) {
				message = (String)object;
			}
			if (object instanceof Throwable) {
				Throwable throwable = (Throwable)object;
				throwable.printStackTrace();
				message = getThrowableMessage(throwable);
			}
			else {
				message = object.toString();
			}
			while (_enum.hasMoreElements()) {
				
				ILogWriter logWriter = (ILogWriter)_enum.nextElement();
				
				logWriter.write(level, category, message, date);				
			}
		}
		else {
			write(level, category, NULL_STR);
		}
	}

	

	
	public static void fatal(Object object) {
		write(Log.FATAL, DEFAULT, object);
	}
	
	public static void error(Object object) {
		write(Log.ERROR, DEFAULT, object);
	}
	
	public static void warn(Object object) {
		write(Log.WARN, DEFAULT, object);
	}
	
	public static void info(Object object) {
		write(Log.INFO, DEFAULT, object);
	}
	
	public static void debug(Object object) {
		write(Log.DEBUG, DEFAULT, object);
	}
	
	public static void trace(Object object) {
		write(Log.TRACE, DEFAULT, object);
	}
	
	
	
	public static void fatal(String category, Object object) {
		write(Log.FATAL, category, object);
	}
	
	public static void error(String category, Object object) {
		write(Log.ERROR, category, object);
	}
	
	public static void warn(String category, Object object) {
		write(Log.WARN, category, object);
	}
	
	public static void info(String category, Object object) {
		write(Log.INFO, category, object);
	}
	
	public static void debug(String category, Object object) {
		write(Log.DEBUG, category, object);
	}
	
	public static void trace(String category, Object object) {
		write(Log.TRACE, category, object);
	}

	public static boolean isLoggable(String category, int level) {
		return true;
	}

	public static int getLevel() {
		return thresholdLevel;
	}

	public static void setThresholdLevel(int thresholdLevel) {
		Log.thresholdLevel = thresholdLevel;
	}

}
