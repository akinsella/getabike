/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.task;

import org.helyx.app.j2me.getabike.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ITask;
import org.helyx.app.j2me.getabike.lib.task.MultiTaskProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.logging4me.Logger;


public class MultiTaskProgressTask extends AbstractProgressTask {

	private static final Logger logger = Logger.getLogger("MULTI_TASK_PROGRESS_TASK");
	
	private ITask[] tasks;
	private int currentIndex;
	private InternalProgressMultiTaskListener internaleProgressListener;
	
	public MultiTaskProgressTask(ITask[] tasks) {
		super(logger.getCategory().getName());
		this.tasks = tasks;
	}

	public String getDescription() {
		return logger.getCategory().getName();
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
			super(MultiTaskProgressTask.logger.getCategory().getName());
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
