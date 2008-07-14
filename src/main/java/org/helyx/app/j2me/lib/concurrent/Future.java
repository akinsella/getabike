package org.helyx.app.j2me.lib.concurrent;

import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;

public class Future {
	
	protected Object response;
	protected boolean failed = false;
	protected RuntimeException exception;
	protected String exceptionMessage;
	protected boolean processed = false;

	
	public synchronized void notifyResponse() {
		notify();
	}
	
	protected void markAsProcessed() {
		processed = true;
	}

	public static Object get(IProgressTask progressTask) {
		final Future future = new Future();
		progressTask.addProgressListener(new ProgressAdapter() {

			public void onCancel(String eventMessage, Object eventData) {
				future.exceptionMessage = eventMessage != null ? eventMessage : "ON_CANCEL";
			}

			public void onError(String eventMessage, Object eventData) {
				future.exceptionMessage = eventMessage != null ? eventMessage : "ON_ERROR";
				if (eventData instanceof RuntimeException) {
					future.exception = (RuntimeException)eventData;
				}	
			}

			public void onSuccess(String eventMessage, Object eventData) {
				future.response = eventData;
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				synchronized (future) {
					future.notify();
				}
				future.markAsProcessed();
			}
			
		});
		
		progressTask.execute();
		
		synchronized (future) {
			try { 
				if (!future.processed) {
					future.wait(); 
				}
			}
			catch(InterruptedException ie) {
				throw new RuntimeException(ie.getMessage() != null ? ie.getMessage() : "Interrupted Exception");
			}			
		}
		
		if (future.failed) {
			if (future.exception != null)  {
				throw future.exception;
			}
			else {
				throw new RuntimeException(future.exceptionMessage);
			}
		}
		
		return future.response;
	}
	
}
