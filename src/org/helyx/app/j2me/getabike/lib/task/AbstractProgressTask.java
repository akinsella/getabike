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

import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressDispatcher;
import org.helyx.app.j2me.getabike.lib.task.ProgressListener;
import org.helyx.logging4me.Logger;


public abstract class AbstractProgressTask implements IProgressTask {

	private static final Logger logger = Logger.getLogger("ABSTRACT_PROGRESS_TASK");
	
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
		if (logger.isDebugEnabled()) {
			logger.debug("Task : '" + taskName + "' is starting");
		}
    	try {
    		final Runnable runnable = getRunnable();
    		thread = new Thread(new Runnable() {

				public void run() {
					long start = System.currentTimeMillis();
					try {
						logger.debug("Starting progress task: '" + taskName + "'");
						alive = true;
						runnable.run();
					}
					finally {
						alive = false;
						if (logger.isDebugEnabled()) {
							long duration = Math.abs(System.currentTimeMillis() - start);
							logger.debug("Progress task: '" + taskName + "' ended. Duration: " + duration + "ms");
						}
					}
				}
    			
    		});
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
