package org.helyx.app.j2me.velocite.task;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.task.AbstractTask;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;

public class SoftKeyConfigurationTask extends AbstractTask {
	
	private static final String CAT = "SOFT_KEY_CONFIGURATION_TASK";
	
	private Canvas canvas;
	
	public SoftKeyConfigurationTask(Canvas canvas) {
		super("Détection des touches");
		this.canvas = canvas;
	}
	
	public void execute() {
		KeyUtil.initKeyMapConfiguration(canvas);
	}

}
