package org.helyx.app.j2me.lib.action;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class EmptyAction extends BasicAction {
	
	public static final Logger logger = LoggerFactory.getLogger("EMPTY_ACTION");
	
	public EmptyAction() {
		super();
	}
	
	public void run(Object data) {
		logger.debug("Empty action hited");
	}
}
