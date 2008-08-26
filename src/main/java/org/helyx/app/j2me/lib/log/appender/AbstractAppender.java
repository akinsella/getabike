package org.helyx.app.j2me.lib.log.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.format.DateFormatUtil;
import org.helyx.app.j2me.lib.log.Log;

public abstract class AbstractAppender implements Appender {

	private int thresholdLevel = Log.DEBUG;

	public AbstractAppender() {
		super();
	}

	public void write(int level, Log log, String message, Date date) {
		try {
			onWrite(level, log, message, date);
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}

	public abstract void onWrite(int level, Log log, String message, Date date);
	
	protected String getLogMessage(int level, Log log, String message, Date date) {
		String dateStr = DateFormatUtil.formatDate(date);
		
		StringBuffer sb = new StringBuffer().append("[").append(log.getLevelName(level)).append(" - ").append(log.getCategory()).append(" - ").append(dateStr).append("] ").append(message);

		return sb.toString();
	}
	
	public int getThresholdLevel() {
		return thresholdLevel;
	}

	public void setThresholdLevel(int thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}

}
