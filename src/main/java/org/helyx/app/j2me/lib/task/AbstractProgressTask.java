package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public abstract class AbstractProgressTask extends Thread implements IProgressTask {
	
	private static final Log log = LogFactory.getLog("ABSTRACT_PROGRESS_TASK");
	
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
		log.debug("Task : '" + taskName + "' is starting");
    	try {
    		execute();
		}
    	catch(Throwable t) {
    		log.warn(t);
    		progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
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
