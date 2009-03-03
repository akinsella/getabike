package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;


public class BasicTask extends AbstractTask {

	private static final Logger logger = LoggerFactory.getLogger("BASIC_TASK");

	public BasicTask(String description) {
		super(description);
	}
	
	public void execute() {
		
	}
	
}
