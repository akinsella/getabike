package org.helyx.app.j2me.lib.manager;

import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.displayable.IAbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.LoadTaskView;
import org.helyx.app.j2me.lib.ui.util.DialogUtil;
import org.helyx.app.j2me.lib.util.ErrorUtil;

public class TaskManager {

	
	private static final String CAT = "TASK_MANAGER";
	
	private TaskManager() {
		super();
	}
	
	public static void runLoadTaskView(String taskDescription, IProgressTask progressTask, final MIDlet midlet, final IAbstractDisplayable currentDisplayable, final IAbstractDisplayable targetDisplayable) {

		final LoadTaskView loadTaskView = new LoadTaskView(midlet, taskDescription, progressTask);
		loadTaskView.setPreviousDisplayable(targetDisplayable);

		progressTask.addProgressListener(new ProgressAdapter(CAT + "[" + progressTask.getDescription() + "]") {

			public void onCancel(String eventMessage, Object eventData) {
				loadTaskView.returnToPreviousDisplayable();
			}

			public void onError(String eventMessage, Object eventData) {
				DialogUtil.showAlertMessage(midlet, currentDisplayable.getDisplayable(), "Erreur", eventMessage != null ? eventMessage : ErrorUtil.getMessage(eventData));
				loadTaskView.returnToPreviousDisplayable();
			}

			public void onSuccess(String eventMessage, Object eventData) {
				loadTaskView.returnToPreviousDisplayable();
			}
			
		});
		
		currentDisplayable.showDisplayable(loadTaskView);
		loadTaskView.startTask();
	}
	
}
