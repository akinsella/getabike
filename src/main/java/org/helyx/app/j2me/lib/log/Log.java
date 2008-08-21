package org.helyx.app.j2me.lib.log;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.text.StringFormat;

public class Log {

	public static final int FATAL = 6;
	public static final int ERROR = 5;
	public static final int WARN = 4;
	public static final int INFO = 3;
	public static final int DEBUG = 2;
	public static final int TRACE = 1;

	public static final String FATAL_STR = "FATAL";
	public static final String ERROR_STR = "ERROR";
	public static final String WARN_STR = "WARN";
	public static final String INFO_STR = "INFO";
	public static final String DEBUG_STR = "DEBUG";
	public static final String TRACE_STR = "TRACE";
	
	private static final String NULL_STR = "null";

	private static Vector logWriterList = new Vector();
	
	private static int thresholdLevel = DEBUG;
	
	static {
		logWriterList = new Vector();
		logWriterList.addElement(ConsoleLogWriter.getInstance());
	}
	
	private String category;
	
	public Log(String category) {
		super();
		this.category = category;
	}

	public String getLevelName(int level) {
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
	
	public void addLogWriter(ILogWriter logWriter) {
		logWriterList.addElement(logWriter);
	}

	public boolean existLogWriter(ILogWriter logWriter) {
		return logWriterList.contains(logWriter);
	}

	public void removeLogWriter(ILogWriter logWriter) {
		logWriterList.removeElement(logWriter);
	}

	public Vector getLogWriterList() {
		
		Vector newLogWriterList = new Vector();
	
		Enumeration _enum = logWriterList.elements();
		
		while (_enum.hasMoreElements()) {	
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			newLogWriterList.addElement(logWriter);
		}
		
		return newLogWriterList;
	}

	public void flushLogWriters() throws Exception {
		Enumeration _enum = logWriterList.elements();
		
		while (_enum.hasMoreElements()) {			
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			logWriter.flush();
		}
	}

	public void closeLogWriters() throws Exception {
		Enumeration _enum = getLogWriterList().elements();

		logWriterList.removeAllElements();

		while (_enum.hasMoreElements()) {			
			ILogWriter logWriter = (ILogWriter)_enum.nextElement();
			logWriter.close();
		}
	}

	private String getThrowableMessage(Throwable throwable) {
		return "Exception : " + throwable.getMessage() + " - " + throwable.getClass().getName();
	}

	private void write(int level, Object object) {
//		System.err.println("level: " + level + ", thresholdLevel: "  + thresholdLevel);
		if (!isLoggable(level)) {
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
				
				logWriter.write(level, this, message, date);				
			}
		}
		else {
			write(level, NULL_STR);
		}
	}

	

	

	public void fatal(Object object) {
		write(Log.FATAL, object);
	}
	
	public void error(Object object) {
		write(Log.ERROR, object);
	}
	
	public void warn(Object object) {
		write(Log.WARN, object);
	}
	
	public void info(Object object) {
		write(Log.INFO, object);
	}
	
	public void debug(Object object) {
		write(Log.DEBUG, object);
	}
	
	public void trace(Object object) {
		write(Log.TRACE, object);
	}

	
	
	
	
	
	public void fatal(String log, Object param) {
		write(Log.FATAL, new StringFormat(log).format(param));
	}
	
	public void error(String log, Object param) {
		write(Log.ERROR, new StringFormat(log).format(param));
	}
	
	public void warn(String log, Object param) {
		write(Log.WARN, new StringFormat(log).format(param));
	}
	
	public void info(String log, Object param) {
		write(Log.INFO, new StringFormat(log).format(param));
	}
	
	public void debug(String log, Object param) {
		write(Log.DEBUG, new StringFormat(log).format(param));
	}
	
	public void trace(String log, Object param) {
		write(Log.TRACE, new StringFormat(log).format(param));
	}
	
	
	
	
	
	public void fatal(String log, Object[] params) {
		write(Log.FATAL, new StringFormat(log).format(params));
	}
	
	public void error(String log, Object[] params) {
		write(Log.ERROR, new StringFormat(log).format(params));
	}
	
	public void warn(String log, Object[] params) {
		write(Log.WARN, new StringFormat(log).format(params));
	}
	
	public void info(String log, Object[] params) {
		write(Log.INFO, new StringFormat(log).format(params));
	}
	
	public void debug(String log, Object[] params) {
		write(Log.DEBUG, new StringFormat(log).format(params));
	}
	
	public void trace(String log, Object[] params) {
		write(Log.TRACE, new StringFormat(log).format(params));
	}


	
	
	public boolean isLoggable(int level) {
		return level > Log.thresholdLevel;
	}
	
	public boolean isFatalEnabled() {
		return FATAL > Log.thresholdLevel;
	}
	
	public boolean isErrorEnabled() {
		return ERROR > Log.thresholdLevel;
	}
	
	public boolean isWarnEnabled() {
		return WARN > Log.thresholdLevel;
	}
	
	public boolean isInfoEnabled() {
		return INFO > Log.thresholdLevel;
	}
	
	public boolean isDebugEnabled() {
		return DEBUG > Log.thresholdLevel;
	}
	
	public boolean isTraceEnabled() {
		return TRACE > Log.thresholdLevel;
	}

	public int getLevel() {
		return thresholdLevel;
	}

	public void setThresholdLevel(int thresholdLevel) {
		Log.thresholdLevel = thresholdLevel;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public static int getThresholdLevel() {
		return thresholdLevel;
	}

}
