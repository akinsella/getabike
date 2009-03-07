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

	private Future() {
		super();
	}
	
	public synchronized void notifyResponse() {
		notify();
	}
	
	protected void markAsProcessed() {
		processed = true;
	}

	public static Object get(IProgressTask progressTask) {
		return get(progressTask, 0);
	}

	public static Object get(IProgressTask progressTask, long timeout) {
		final Future future = new Future();
		progressTask.addProgressListener(new ProgressAdapter(logger.getCategory()) {

			public void onCancel(String eventMessage, Object eventData) {
				buildFutureException(future, eventMessage, eventData, "ON_CANCEL");
			}

			public void onError(String eventMessage, Object eventData) {
				buildFutureException(future, eventMessage, eventData, "ON_ERROR");
			}

			private void buildFutureException(Future future, String eventMessage, Object eventData, String defaultMessage) {
				future.failed = true;
				future.exceptionMessage = eventMessage != null ? eventMessage : defaultMessage;
				if (eventData instanceof RuntimeException) {
					future.exception = (RuntimeException)eventData;
				}
				else if(eventData instanceof Error) {
					Throwable error = (Throwable)eventData;
					future.exception = new FutureException(error);
				}
				else {
					future.exception = new FutureException(future.exceptionMessage);
				}
			}

			public void onSuccess(String eventMessage, Object eventData) {
				future.response = eventData;
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				future.markAsProcessed();
				synchronized (future) {
					future.notify();
				}
			}
			
		});
		
		progressTask.execute();
		
		synchronized (future) {
			try { 
				if (!future.processed) {
					if (timeout > 0)  {
						future.wait(timeout);
						if (!future.processed) {
							if (logger.isDebugEnabled()) {
								logger.debug("Future timeout reached");
								logger.debug("Interrupting progress task");
							}
							progressTask.interrupt();
							throw new FutureTimeoutException("Future timeout reached");
						}
					}
					else {
						future.wait();
					}
				}
			}
			catch(InterruptedException ie) {
				throw new FutureException(ie.getMessage() != null ? ie.getMessage() : "Interrupted Exception");
			}
			catch(FutureException fe) {
				throw fe;
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
