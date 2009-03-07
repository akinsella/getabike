package org.helyx.app.j2me.lib.logger;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.helyx.app.j2me.lib.logger.appender.Appender;
import org.helyx.app.j2me.lib.logger.appender.ConsoleAppender;
import org.helyx.app.j2me.lib.logger.appender.FileAppender;

public class LoggerManager {
	
	private static Vector appenderList = new Vector();
	
	private static Hashtable categoryMap = new Hashtable();
	
	private static int thresholdLevel = Logger.DEBUG;
	
	static {
		Appender consoleAppender = ConsoleAppender.getInstance();
		appenderList.addElement(consoleAppender);
	}
	
	private LoggerManager(String category) {
		super();
	}
	
	public static void addAppender(Appender appender) {
		appenderList.addElement(appender);
	}

	public static void addAppenderAndOpen(FileAppender appender) throws Exception {
		appender.open();
		addAppender(appender);
	}

	public static boolean existAppender(Appender appender) {
		return appenderList.contains(appender);
	}

	public static void removeAppender(Appender appender) {
		appenderList.removeElement(appender);
	}

	public static void removeAppenderAndClose(FileAppender appender) {
		removeAppender(appender);
		try {
			try {
				appender.flush();
			}
			finally {
				appender.close();
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
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

	public static void flushAppenders() {
		Enumeration _enum = appenderList.elements();
		
		while (_enum.hasMoreElements()) {			
			try {
				Appender appender = (Appender)_enum.nextElement();
				appender.flush();
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public static void closeAllAppenders() {
		Enumeration _enum = getAppenderList().elements();

		appenderList.removeAllElements();

		while (_enum.hasMoreElements()) {			
			Appender appender = (Appender)_enum.nextElement();
			try {
				try {
					appender.flush();
				}
				finally {
					appender.close();
				}
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	public static void removeAllCateogries() {
		categoryMap.clear();
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
