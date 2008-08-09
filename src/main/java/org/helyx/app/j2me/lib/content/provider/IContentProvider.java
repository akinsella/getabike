package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.IProgressListenerManager;

public interface IContentProvider extends IProgressListenerManager {
	
	String getCat();
	
	IProgressDispatcher getProgressDispatcher();
	
	void loadData();
	
	void cancel();

	String getDescription();
	
}
