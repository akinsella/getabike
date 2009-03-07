package org.helyx.app.j2me.lib.task;


public interface IProgressTask extends ITask, IProgressListenerManager {
	
	String getName();

	boolean isCancellable();

	void cancel();

	void execute();
	
	Runnable getRunnable();
	
	IProgressDispatcher getProgressDispatcher();

	void interrupt();
	
	boolean isAlive();

}
