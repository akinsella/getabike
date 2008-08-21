package org.helyx.app.j2me.lib.action;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class EmptyAction extends BasicAction {
	
	public static final Log log = LogFactory.getLog("EMPTY_ACTION");
	
	public EmptyAction() {
		super();
	}
	
	public void run(Object data) {
		log.debug("Empty action hited");
	}
}
