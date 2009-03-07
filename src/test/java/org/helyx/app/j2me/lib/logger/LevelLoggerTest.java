package org.helyx.app.j2me.lib.logger;

import java.util.Date;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.logger.appender.ConsoleAppender;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.task.IProgressTask;

public class LevelLoggerTest extends TestCase {
	
	protected static final String TEST_CATEGORY = "TEST_CATEGORY";
	
	protected static final String TEST_APPENDER = "TEST_APPENDER";
	
	protected static final String TEST_CONTENT = "TEST_CONTENT";

	
	public void testLogger() throws InterruptedException {
		
		LoggerManager.setThresholdLevel(Logger.DEBUG);
		
		LoggerManager.removeAllCateogries();
		LoggerManager.closeAllAppenders();
		LoggerManager.addAppender(ConsoleAppender.getInstance());
		
		LoggerManager.addCategory(TEST_CATEGORY, TEST_APPENDER, Logger.INFO);

		final Logger logger = LoggerFactory.getLogger(TEST_CATEGORY);

		IProgressTask loggerTestProgressTask = new LoggerTestProgressTask(TEST_APPENDER) {

			public void log() {
				logger.info(TEST_CONTENT);
			}

			public void checkLog(int level, Logger logger, String message, Date date) {
				if (level == Logger.INFO && message.equals(TEST_CONTENT)) {
					getProgressDispatcher().fireEvent(EventType.ON_SUCCESS, Boolean.TRUE);
				}
			}
			
		};
		
		Boolean result = Boolean.FALSE;
		try {
			result = (Boolean)Future.get(loggerTestProgressTask, 3000);
		}
		catch(Throwable fte) {
			fail(fte.getMessage());
		}
		
		
		logger.info(TEST_CONTENT);
		
		logger.info("is alive: " + loggerTestProgressTask.isAlive());
		
		if (result != null && !result.booleanValue()) {
			fail("Fail to log infomation");
		}
	}

}
