package org.helyx.app.j2me.lib.manager;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.LoadTaskView;

public class TaskManager {
	
	private static final Log log = LogFactory.getLog("TASK_MANAGER");
	
	private TaskManager() {
		super();
	}

	public static void runLoadTaskView(String taskDescription, IProgressTask progressTask, final AbstractMIDlet midlet, final AbstractDisplayable currentDisplayable) {
		runLoadTaskView(taskDescription, progressTask, midlet, currentDisplayable, null);
	}
	
	public static void runLoadTaskView(String taskDescription, IProgressTask progressTask, final AbstractMIDlet midlet, final AbstractDisplayable currentDisplayable, final IReturnCallback returnCallback) {

		final LoadTaskView loadTaskView = new LoadTaskView(midlet, taskDescription, progressTask);
		loadTaskView.setReturnCallback(returnCallback);

		progressTask.addProgressListener(new ProgressAdapter() {

			public void onCancel(String eventMessage, Object eventData) {
				loadTaskView.fireReturnCallback();
			}

			public void onError(String eventMessage, Object eventData) {
//					DialogUtil.showAlertMessage(midlet, currentDisplayable.getDisplayable(), "Erreur", eventMessage != null ? eventMessage : ErrorUtil.getMessage(eventData));
				loadTaskView.fireReturnCallback();
			}

			public void onSuccess(String eventMessage, Object eventData) {
				loadTaskView.fireReturnCallback();
			}
			
		});
		
		currentDisplayable.showDisplayable(loadTaskView);
		loadTaskView.startTask();
	}
	
}
