package org.helyx.app.j2me.lib.logger;

import java.util.Date;

import org.helyx.app.j2me.lib.logger.appender.AbstractAppender;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;

public abstract class LoggerTestProgressTask extends AbstractProgressTask {
	
	private static final Logger logger = LoggerFactory.getLogger("LoggerTestProgressTask");
	private static final String LOGGER_TEST_PROGRESS_TASK = "LOGGER_TEST_PROGRESS_TASK";
	
	private String appenderName;
	
	public LoggerTestProgressTask(String appenderName) {
		super(LOGGER_TEST_PROGRESS_TASK);
		this.appenderName = appenderName;
	}
	
	public Runnable getRunnable() {
		
		return new Runnable() {

			public void run() {
				LoggerManager.addAppender(new AbstractAppender() {

					public void onWrite(int level, Logger logger, String message, Date date) {
						checkLog(level, logger, message, date);
					}

					public void close() throws Exception {
						System.out.println("Closing '" + getName() + "' appender");
					}

					public void flush() throws Exception {
						System.out.println("Flushing '" + getName() + "' appender");
					}

					public String getName() {
						return appenderName;
					}

					public void open() throws Exception {
						System.out.println("Opening '" + getName() + "' appender");
					}
					
				});
				log();
				logger.info("Logger progress task ended");
			}
			
		};
				
	}
	
	public abstract void log();
	
	public abstract void checkLog(int level, Logger logger, String message, Date date);

}

