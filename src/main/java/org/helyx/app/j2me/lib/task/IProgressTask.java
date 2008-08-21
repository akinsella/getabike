package org.helyx.app.j2me.lib.task;


public interface IProgressTask extends ITask, IProgressListenerManager {
	
	String getName();

	boolean isCancellable();

	void cancel();

	void start();
	
	void execute();
	
	IProgressDispatcher getProgressDispatcher();

}
