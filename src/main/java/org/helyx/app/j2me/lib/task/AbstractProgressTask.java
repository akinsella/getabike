package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public abstract class AbstractProgressTask extends Thread implements IProgressTask {
	
	private static final Logger logger = LoggerFactory.getLogger("ABSTRACT_PROGRESS_TASK");
	
	protected IProgressDispatcher progressDispatcher;
	protected String taskName;
	protected boolean cancellable = false;

	public AbstractProgressTask(String taskName) {
		super();
		this.taskName = taskName;
		progressDispatcher = new ProgressDispatcher();
		progressDispatcher.setName(taskName);
	}
	
	public void addProgressListener(ProgressListener progressListener) {
		progressDispatcher.addProgressListener(progressListener);
	}

	public void run() {
		logger.debug("Task : '" + taskName + "' is starting");
    	try {
    		execute();
		}
    	catch(Throwable t) {
    		logger.warn(t);
    		progressDispatcher.fireEvent(EventType.ON_ERROR, t);
    	}
 	}

	public void removeProgressListener(ProgressListener progressListener) {
		progressDispatcher.removeProgressListener(progressListener);
	}

	public String getTaskName() {
		return taskName;
	}

	public String getDescription() {
		return taskName;
	}

	public Object getResult() {
		return null;
	}
	
	public void cancel() {
		
	}

	public boolean isCancellable() {
		return cancellable;
	}

	public IProgressDispatcher getProgressDispatcher() {
		return progressDispatcher;
	}
	
}
