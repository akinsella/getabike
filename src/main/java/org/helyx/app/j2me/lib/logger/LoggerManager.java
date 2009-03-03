package org.helyx.app.j2me.lib.logger;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.helyx.app.j2me.lib.logger.appender.Appender;
import org.helyx.app.j2me.lib.logger.appender.ConsoleAppender;

public class LoggerManager {

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

	private static Vector appenderList = new Vector();
	
	private static Hashtable categoryMap = new Hashtable();
	
	private static int thresholdLevel = DEBUG;
	
	static {
		Appender consoleAppender = ConsoleAppender.getInstance();
		appenderList.addElement(consoleAppender);
	}
	
	private String category;
	
	private LoggerManager(String category) {
		super();
	}
	
	public static void addAppender(Appender appender) {
		appenderList.addElement(appender);
	}

	public static boolean existAppender(Appender appender) {
		return appenderList.contains(appender);
	}

	public static void removeAppender(Appender appender) {
		appenderList.removeElement(appender);
	}

	public static Vector getAppenderList() {
		
		Vector newAppenderList = new Vector();
	
		Enumeration _enum = appenderList.elements();
		
		while (_enum.hasMoreElements()) {
			Appender appender = (Appender)_enum.nextElement();
			newAppenderList.addElement(appender);
		}
		
		return newAppenderList;
	}

	public static void flushAppenders() throws Exception {
		Enumeration _enum = appenderList.elements();
		
		while (_enum.hasMoreElements()) {			
			Appender appender = (Appender)_enum.nextElement();
			appender.flush();
		}
	}

	public static void closeAppenders() throws Exception {
		Enumeration _enum = getAppenderList().elements();

		appenderList.removeAllElements();

		while (_enum.hasMoreElements()) {			
			Appender appender = (Appender)_enum.nextElement();
			appender.close();
		}
	}


	public static void setThresholdLevel(int thresholdLevel) {
		LoggerManager.thresholdLevel = thresholdLevel;
	}

	public static int getThresholdLevel() {
		return thresholdLevel;
	}

	public static void addCategory(String categoryName, String appenderName, int level) {
		Category category = new Category(categoryName, appenderName, level);
		Hashtable appenderMap = (Hashtable)LoggerManager.categoryMap.get(categoryName);
		if (appenderMap == null) {
			appenderMap = new Hashtable();
			LoggerManager.categoryMap.put(categoryName, appenderMap);
		}
		appenderMap.put(appenderName, category);				
	}

	public static Category findCategory(String categoryName, String appenderName) {
		Hashtable appenderMap = (Hashtable)LoggerManager.categoryMap.get(categoryName);
		if (appenderMap == null) {
			return null;			
		}
		Category category = (Category)appenderMap.get(appenderName);
		
		if (category == null) {
			category = (Category)appenderMap.get(Category.WILCARD_APPENDER);
		}
		
		return category;
	}

	public static void addCategory(String categoryName, int level) {
		addCategory(categoryName, Category.WILCARD_APPENDER, level);
	}

}
