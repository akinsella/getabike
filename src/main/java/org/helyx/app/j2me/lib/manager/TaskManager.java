package org.helyx.app.j2me.lib.manager;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.Event;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.LoadTaskView;

public class TaskManager {
	
	private static final Logger logger = LoggerFactory.getLogger("TASK_MANAGER");
	
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
