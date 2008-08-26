package org.helyx.app.j2me.lib.log.appender;

import java.util.Date;

import org.helyx.app.j2me.lib.log.Log;


public interface Appender {

	public void write(int level, Log log, String message, Date date);

	public void open() throws Exception;

	public void close() throws Exception;

	public void flush() throws Exception;

	public int getThresholdLevel();

	public void setThresholdLevel(int thresholdLevel);

}
