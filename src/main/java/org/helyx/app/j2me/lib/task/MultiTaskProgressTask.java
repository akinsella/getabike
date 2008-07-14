package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;

public class MultiTaskProgressTask extends AbstractProgressTask {

	private static final String CAT = "MULTI_TASK_PROGRESS_TASK";
	
	private ITask[] progressTasks;
	private int currentIndex;
	private InternalProgressMultiTaskListener internaleProgressListener;
	
	public MultiTaskProgressTask(ITask[] progressTasks) {
		super(CAT);
		this.progressTasks = progressTasks;
	}

	public String getDescription() {
		return getName();
	}

	public boolean isCancellable() {
		return false;
	}
	
	public void execute() {
		int taskToRunCount = progressTasks.length;
		Log.debug(CAT, "Starting Multitask executor");
		Log.info(CAT, "Scheduled task: ");
		for (int i = 0 ; i < taskToRunCount ; i++) {
			Log.info(CAT, i + " - " + progressTasks[i].getDescription());
		}

		internaleProgressListener = new InternalProgressMultiTaskListener();
		progressDispatcher.fireEvent(ProgressEventType.ON_START);

		startCurrentTask();
	}
	
	private void startCurrentTask() {
		int length = progressTasks.length;
		if (currentIndex >= length) {
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS);
			return;
		}
		ITask currentTask = progressTasks[currentIndex];

		if (currentTask instanceof IProgressTask) {
			IProgressTask currentProgressTask = (IProgressTask)currentTask;
			Log.debug(CAT, "Starting Progress task : '" + currentTask.getDescription() + "'.");
			currentProgressTask.addProgressListener(internaleProgressListener);
			currentProgressTask.start();
		}
		else {
			try {
				Log.debug(CAT, "Starting basic task. Task description: '" + currentTask.getDescription() + "'.");
				currentTask.execute();
			}
			catch(Throwable t) { 
				Log.warn(CAT, t);
				progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t.getMessage());
				cleanUpCurrentTask();
				return;
			}
			currentIndex++;
			startCurrentTask();
		}
	}

	private void cleanUpCurrentTask() {
		ITask currentTask = progressTasks[currentIndex];
	
		if (currentTask instanceof IProgressTask) {
			IProgressTask currentProgressTask = (IProgressTask)currentTask;
			Log.debug(CAT, "Cleaning up progress task: '" + currentTask.getDescription() + "'.");
			currentProgressTask.removeProgressListener(internaleProgressListener);
		}
		else {
			Log.debug(CAT, "Cleaning up basic task: '" + currentTask.getDescription() + "'.");			
		}
	}

	class InternalProgressMultiTaskListener extends ProgressAdapter {

		public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, eventMessage);
		}

		public void onProgress(String eventMessage, Object eventData) {
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, eventMessage);
		}

		public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
			if (eventType == ProgressEventType.ON_SUCCESS) {
				Log.info(CAT, "ON_SUCCESS");
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
