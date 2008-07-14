package org.helyx.app.j2me.lib.task;


public abstract class AbstractTask implements ITask {

	private static final String CAT = "ABSTRACT_TASK";
	
	private String description;
	
	public AbstractTask(String description) {
		super();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
