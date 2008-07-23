package org.helyx.app.j2me.lib.ui.widget.action;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.widget.BasicAction;

public class EmptyAction extends BasicAction {
	
	public static final String CAT = "EMPTY_ACTION";
	
	public EmptyAction() {
		super();
	}
	
	public void run(Object data) {
		Log.debug(CAT, "Empty action hited");
	}
}
