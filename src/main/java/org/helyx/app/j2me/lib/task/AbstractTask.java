package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;


public abstract class AbstractTask implements ITask {

	private static final Logger logger = LoggerFactory.getLogger("ABSTRACT_TASK");
	
	private String description;
	
	public AbstractTask(String description) {
		super();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
