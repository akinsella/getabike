package org.helyx.app.j2me.lib.concurrent;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;

public class Future {
	
	private static final Logger logger = LoggerFactory.getLogger("FUTURE");
	
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

	public static Object getSync(IProgressTask progressTask) {
		return get(progressTask, false);
	}

	public static Object getASync(IProgressTask progressTask) {
		return get(progressTask, true);
	}

	public static Object get(IProgressTask progressTask) {
		return getASync(progressTask);
	}

	public static Object get(IProgressTask progressTask, boolean async) {
		final Future future = new Future();
		progressTask.addProgressListener(new ProgressAdapter(logger.getCategory()) {

			public void onCancel(String eventMessage, Object eventData) {
				future.failed = true;
				future.exceptionMessage = eventMessage != null ? eventMessage : "ON_CANCEL";
			}

			public void onError(String eventMessage, Object eventData) {
				future.failed = true;
				future.exceptionMessage = eventMessage != null ? eventMessage : "ON_ERROR";
				if (eventData instanceof FutureException) {
					future.exception = (FutureException)eventData;
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
		
		if (async) {
			progressTask.start();
		}
		else {
			progressTask.execute();
		}
		
		synchronized (future) {
			try { 
				if (!future.processed) {
					future.wait(); 
				}
			}
			catch(InterruptedException ie) {
				throw new FutureException(ie.getMessage() != null ? ie.getMessage() : "Interrupted Exception");
			}
			catch(Throwable t) {
				throw new FutureException(t);
			}
			
		}
		
		if (future.failed) {
			if (future.exception != null)  {
				throw future.exception;
			}
			else {
				throw new FutureException(future.exceptionMessage);
			}
		}
		
		return future.response;
	}
	
}
