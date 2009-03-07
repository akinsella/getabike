package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class MultiTaskProgressTask extends AbstractProgressTask {

	private static final Logger logger = LoggerFactory.getLogger("MULTI_TASK_PROGRESS_TASK");
	
	private ITask[] tasks;
	private int currentIndex;
	private InternalProgressMultiTaskListener internaleProgressListener;
	
	public MultiTaskProgressTask(ITask[] tasks) {
		super(logger.getCategory());
		this.tasks = tasks;
	}

	public String getDescription() {
		return logger.getCategory();
	}

	public boolean isCancellable() {
		return false;
	}
	
	public Runnable getRunnable() {
		
		return new Runnable() {

			public void run() {
				int taskToRunCount = tasks.length;
				logger.debug("Starting Multitask executor");
				logger.info("Scheduled task: ");
				for (int i = 0 ; i < taskToRunCount ; i++) {
					logger.info(i + " - " + tasks[i].getDescription());
				}

				internaleProgressListener = new InternalProgressMultiTaskListener();
				progressDispatcher.fireEvent(EventType.ON_START);

				startCurrentTask();
			}
			
		};
	}
	
	private void startCurrentTask() {
		int length = tasks.length;
		if (currentIndex >= length) {
			progressDispatcher.fireEvent(EventType.ON_SUCCESS);
			return;
		}
		ITask currentTask = tasks[currentIndex];

		if (currentTask instanceof IProgressTask) {
			IProgressTask currentProgressTask = (IProgressTask)currentTask;
			logger.debug("Starting Progress task : '" + currentTask.getDescription() + "'.");
			currentProgressTask.addProgressListener(internaleProgressListener);
			currentProgressTask.execute();
		}
		else {
			try {
				logger.debug("Starting basic task. Task description: '" + currentTask.getDescription() + "'.");
				currentTask.execute();
			}
			catch(Throwable t) { 
				logger.warn(t);
				progressDispatcher.fireEvent(EventType.ON_ERROR, t.getMessage());
				cleanUpCurrentTask();
				return;
			}
			currentIndex++;
			startCurrentTask();
		}
	}

	private void cleanUpCurrentTask() {
		ITask currentTask = tasks[currentIndex];
	
		if (currentTask instanceof IProgressTask) {
			IProgressTask currentProgressTask = (IProgressTask)currentTask;
			logger.debug("Cleaning up progress task: '" + currentTask.getDescription() + "'.");
			currentProgressTask.removeProgressListener(internaleProgressListener);
		}
		else {
			logger.debug("Cleaning up basic task: '" + currentTask.getDescription() + "'.");			
		}
	}

	class InternalProgressMultiTaskListener extends ProgressAdapter {
	
		public InternalProgressMultiTaskListener() {
			super(MultiTaskProgressTask.logger.getCategory());
		}
		
		public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage);
		}

		public void onProgress(String eventMessage, Object eventData) {
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage);
		}
		
		public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
			if (eventType == EventType.ON_SUCCESS) {
				cleanUpCurrentTask();
				currentIndex++;
				startCurrentTask();
			}
			else {
				progressDispatcher.fireEvent(eventType, eventMessage, eventData);
			}
		}

	}
	
}
