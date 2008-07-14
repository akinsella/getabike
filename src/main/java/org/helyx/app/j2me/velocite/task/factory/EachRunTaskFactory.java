package org.helyx.app.j2me.velocite.task.factory;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;
import org.helyx.app.j2me.velocite.task.SoftKeyConfigurationTask;

public class EachRunTaskFactory implements ITaskFactory {
	
	private static final String CAT = "EACH_RUN_TASK_FACTORY";
	
	private Canvas canvas;
	
	public EachRunTaskFactory(Canvas canvas) {
		super();
		this.canvas = canvas;
	}
	
	public ITask[] getTasks() {
		
		ITask[] tasks = new ITask[] {
			new SoftKeyConfigurationTask(canvas)
		};
		
		return tasks;
	}
}
