package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public abstract class AbstractProgressTask implements IProgressTask {

	private static final Logger logger = LoggerFactory.getLogger("ABSTRACT_PROGRESS_TASK");
	
	protected IProgressDispatcher progressDispatcher;
	protected String taskName;
	protected boolean cancellable = false;
	protected Thread thread;
	protected boolean alive = false;

	public AbstractProgressTask(String taskName) {
		super();
		this.taskName = taskName;
		progressDispatcher = new ProgressDispatcher();
		progressDispatcher.setName(taskName);
	}
	
	public String getName() {
		return taskName;
	}

	public void addProgressListener(ProgressListener progressListener) {
		progressDispatcher.addProgressListener(progressListener);
	}

	public abstract Runnable getRunnable();
	
	public void interrupt() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void execute() {
		if (thread != null) {
			throw new RuntimeException("Task has been already executed");
		}
		logger.debug("Task : '" + taskName + "' is starting");
    	try {
    		final Runnable runnable = getRunnable();
    		thread = new Thread(new Runnable() {

				public void run() {
					try {
						alive = true;
						runnable.run();
					}
					finally {
						logger.info("Runnable ended");
						alive = false;
					}
				}
    			
    		}, taskName);
    		thread.start();
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
