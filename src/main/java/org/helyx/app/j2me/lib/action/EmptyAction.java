package org.helyx.app.j2me.lib.action;

import org.helyx.app.j2me.lib.log.Log;

public class EmptyAction extends BasicAction {
	
	public static final String CAT = "EMPTY_ACTION";
	
	public EmptyAction() {
		super();
	}
	
	public void run(Object data) {
		Log.debug(CAT, "Empty action hited");
	}
}