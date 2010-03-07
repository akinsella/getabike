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
package org.helyx.app.j2me.getabike.lib.manager;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.task.Event;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.task.LoadTaskView;
import org.helyx.logging4me.Logger;


public class TaskManager {
	
	private static final Logger logger = Logger.getLogger("TASK_MANAGER");
	
	private TaskManager() {
		super();
	}

	public static LoadTaskView runLoadTaskView(String taskDescription, IProgressTask progressTask, final AbstractMIDlet midlet, final AbstractDisplayable currentDisplayable) {
		return runLoadTaskView(taskDescription, progressTask, midlet, currentDisplayable, null);
	}
	
	public static LoadTaskView runLoadTaskView(String taskDescription, IProgressTask progressTask, final AbstractMIDlet midlet, final AbstractDisplayable currentDisplayable, final IReturnCallback returnCallback) {

		final LoadTaskView loadTaskView = new LoadTaskView(midlet, taskDescription, progressTask);
		loadTaskView.setReturnCallback(returnCallback);

		progressTask.addProgressListener(new ProgressAdapter("TASK_MANAGER_LISTENER") {
			
			

			public void onSuccess(String eventMessage, Object eventData) {
				loadTaskView.fireReturnCallback(new Event(EventType.ON_SUCCESS, eventMessage, eventData));
			}

			public void onError(String eventMessage, Object eventData) {
				loadTaskView.fireReturnCallback(new Event(EventType.ON_ERROR, eventMessage, eventData));
			}

			public void onCancel(String eventMessage, Object eventData) {
				loadTaskView.fireReturnCallback(new Event(EventType.ON_CANCEL, eventMessage, eventData));
			}
		
		});
		
		currentDisplayable.showDisplayable(loadTaskView);
		loadTaskView.startTask();
		
		return loadTaskView;
	}

}
