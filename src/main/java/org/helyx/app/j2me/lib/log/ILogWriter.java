package org.helyx.app.j2me.lib.log;

import java.util.Date;


public interface ILogWriter {

	public void write(int level, String type, String message, Date date);

	public void open() throws Exception;

	public void close() throws Exception;

	public void flush() throws Exception;
	
}
