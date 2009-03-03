package org.helyx.app.j2me.lib.logger;

import java.util.Date;
import java.util.Enumeration;

import org.helyx.app.j2me.lib.logger.appender.Appender;
import org.helyx.app.j2me.lib.text.StringFormat;

public class Logger {

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
	
	private String category;
	
	public Logger(String category) {
		super();
		this.category = category;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLevelName(int level) {
		switch(level) {
			case Logger.FATAL:
				return FATAL_STR;
			case Logger.ERROR:
				return ERROR_STR;
			case Logger.WARN:
				return WARN_STR;
			case Logger.INFO:
				return INFO_STR;
			case Logger.DEBUG:
				return DEBUG_STR;
			case Logger.TRACE:
				return TRACE_STR;
			default:
				return INFO_STR;
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
		
		if (object == null) {
			write(level, NULL_STR);
			return;
		}

		Date date = new Date();

		Enumeration _enum = LoggerManager.getAppenderList().elements();
		
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
			
			Appender appender = (Appender)_enum.nextElement();
			
			String appenderName = appender.getName();

			Category category = (Category)LoggerManager.findCategory(this.category, appenderName);
			if (category != null) {
				if (category.isLoggable(level)) {
					appender.write(level, this, message, date);	
				}
			}
			else {
				appender.write(level, this, message, date);	
			}				
		}
	}

	

	public void fatal(Object object) {
		write(Logger.FATAL, object);
	}
	
	public void error(Object object) {
		write(Logger.ERROR, object);
	}
	
	public void warn(Object object) {
		write(Logger.WARN, object);
	}
	
	public void info(Object object) {
		write(Logger.INFO, object);
	}
	
	public void debug(Object object) {
		write(Logger.DEBUG, object);
	}
	
	public void trace(Object object) {
		write(Logger.TRACE, object);
	}

	
	
	
	
	
	public void fatal(String logger, Object param) {
		write(Logger.FATAL, new StringFormat(logger).format(param));
	}
	
	public void error(String logger, Object param) {
		write(Logger.ERROR, new StringFormat(logger).format(param));
	}
	
	public void warn(String logger, Object param) {
		write(Logger.WARN, new StringFormat(logger).format(param));
	}
	
	public void info(String logger, Object param) {
		write(Logger.INFO, new StringFormat(logger).format(param));
	}
	
	public void debug(String logger, Object param) {
		write(Logger.DEBUG, new StringFormat(logger).format(param));
	}
	
	public void trace(String logger, Object param) {
		write(Logger.TRACE, new StringFormat(logger).format(param));
	}
	
	
	
	
	
	public void fatal(String logger, Object[] params) {
		write(Logger.FATAL, new StringFormat(logger).format(params));
	}
	
	public void error(String logger, Object[] params) {
		write(Logger.ERROR, new StringFormat(logger).format(params));
	}
	
	public void warn(String logger, Object[] params) {
		write(Logger.WARN, new StringFormat(logger).format(params));
	}
	
	public void info(String logger, Object[] params) {
		write(Logger.INFO, new StringFormat(logger).format(params));
	}
	
	public void debug(String logger, Object[] params) {
		write(Logger.DEBUG, new StringFormat(logger).format(params));
	}
	
	public void trace(String logger, Object[] params) {
		write(Logger.TRACE, new StringFormat(logger).format(params));
	}


		
	public boolean isLoggable(int level) {
		return level >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isFatalEnabled() {
		return FATAL >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isErrorEnabled() {
		return ERROR >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isWarnEnabled() {
		return WARN >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isInfoEnabled() {
		return INFO >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isDebugEnabled() {
		return DEBUG >= LoggerManager.getThresholdLevel();
	}
	
	public boolean isTraceEnabled() {
		return TRACE >= LoggerManager.getThresholdLevel();
	}

}
