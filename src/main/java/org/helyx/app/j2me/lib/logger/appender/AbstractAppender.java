package org.helyx.app.j2me.lib.logger.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.format.DateFormatUtil;
import org.helyx.app.j2me.lib.logger.Logger;

public abstract class AbstractAppender implements Appender {

	private int thresholdLevel = Logger.DEBUG;

	public AbstractAppender() {
		super();
	}

	public void write(int level, Logger logger, String message, Date date) {
		try {
			onWrite(level, logger, message, date);
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}

	public abstract void onWrite(int level, Logger logger, String message, Date date);
	
	protected String getLogMessage(int level, Logger logger, String message, Date date) {
		String dateStr = DateFormatUtil.formatDate(date);
		
		StringBuffer sb = new StringBuffer().append("[").append(logger.getLevelName(level)).append(" - ").append(logger.getCategory()).append(" - ").append(dateStr).append("] ").append(message);

		return sb.toString();
	}
	
	public int getThresholdLevel() {
		return thresholdLevel;
	}

	public void setThresholdLevel(int thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}

}
