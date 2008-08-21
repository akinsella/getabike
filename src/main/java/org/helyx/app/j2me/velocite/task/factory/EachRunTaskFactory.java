package org.helyx.app.j2me.velocite.task.factory;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.ITaskFactory;
import org.helyx.app.j2me.velocite.task.LanguageConfigurationTask;
import org.helyx.app.j2me.velocite.task.SoftKeyConfigurationTask;

public class EachRunTaskFactory implements ITaskFactory {
	
	private static final Log log = LogFactory.getLog("EACH_RUN_TASK_FACTORY");
	
	private AbstractMIDlet midlet;
	private Canvas canvas;
	
	public EachRunTaskFactory(AbstractMIDlet midlet, Canvas canvas) {
		super();
		this.midlet = midlet;
		this.canvas = canvas;
	}
	
	public ITask[] getTasks() {
		
		
		
		ITask[] tasks = new ITask[] {
			new LanguageConfigurationTask(midlet, canvas),
			new SoftKeyConfigurationTask(canvas)
		};
		
		return tasks;
	}
}
