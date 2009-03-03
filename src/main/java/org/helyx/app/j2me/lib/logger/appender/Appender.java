package org.helyx.app.j2me.lib.logger.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.logger.Logger;


public interface Appender {
	
	public String getName();

	public void write(int level, Logger logger, String message, Date date);

	public void open() throws Exception;

	public void close() throws Exception;

	public void flush() throws Exception;

	public int getThresholdLevel();

	public void setThresholdLevel(int thresholdLevel);

}
