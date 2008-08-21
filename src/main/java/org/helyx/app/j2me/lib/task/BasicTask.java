package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;


public class BasicTask extends AbstractTask {

	private static final Log log = LogFactory.getLog("BASIC_TASK");

	public BasicTask(String description) {
		super(description);
	}
	
	public void execute() {
		
	}
	
}
