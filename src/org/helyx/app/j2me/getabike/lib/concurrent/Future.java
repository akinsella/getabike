/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.concurrent;

import org.helyx.app.j2me.getabike.lib.concurrent.Future;
import org.helyx.app.j2me.getabike.lib.concurrent.FutureException;
import org.helyx.app.j2me.getabike.lib.concurrent.FutureTimeoutException;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.logging4me.Logger;


public class Future {
	
	private static final Logger logger = Logger.getLogger("FUTURE");
	
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
		progressTask.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {

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
					Error error = (Error)eventData;
					future.exception = new FutureException(error);
				}
				else if(eventData instanceof Exception) {
					Exception exception = (Exception)eventData;
					future.exception = new FutureException(exception);
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
