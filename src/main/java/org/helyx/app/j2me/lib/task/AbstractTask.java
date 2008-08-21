package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;


public abstract class AbstractTask implements ITask {

	private static final Log log = LogFactory.getLog("ABSTRACT_TASK");
	
	private String description;
	
	public AbstractTask(String description) {
		super();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
