package org.helyx.app.j2me.lib.logger;

public class Category {

	public static String WILCARD_APPENDER = "*";
	
	public String categoryName;
	
	public int level;
	
	public String appenderName;

	public Category(String categoryName, String appenderName, int level) {
		super();
		this.categoryName = categoryName;
		this.appenderName = appenderName;
		this.level = level;
	}

	public Category(String categoryName, int level) {
		super();
		this.categoryName = categoryName;
		this.appenderName = WILCARD_APPENDER;
		this.level = level;
	}

	public Category() {
		super();
	}
	
	public boolean isLoggable(int level) {
		return level >= this.level;
	}
	
	public boolean isFatalEnabled() {
		return Logger.FATAL >= level;
	}
	
	public boolean isErrorEnabled() {
		return Logger.ERROR >= level;
	}
	
	public boolean isWarnEnabled() {
		return Logger.WARN >= level;
	}
	
	public boolean isInfoEnabled() {
		return Logger.INFO >= level;
	}
	
	public boolean isDebugEnabled() {
		return Logger.DEBUG >= level;
	}
	
	public boolean isTraceEnabled() {
		return Logger.TRACE >= level;
	}

}
