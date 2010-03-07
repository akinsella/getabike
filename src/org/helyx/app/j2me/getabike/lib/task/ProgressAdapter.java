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
package org.helyx.app.j2me.getabike.lib.task;

import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.ProgressListener;
import org.helyx.logging4me.Logger;


public class ProgressAdapter implements ProgressListener {

	protected static Logger logger = Logger.getLogger("PROGRESS_ADAPTER");
	private String name;
	
	public ProgressAdapter(String name) {
		super();
		this.name = name;
	}
	
	public void onEvent(int eventType, String eventMessage, Object eventData) {
		
		logEventMessage(eventType, eventMessage, eventData);
		if (eventType == EventType.ON_START) {
	   		onStart(eventMessage, eventData);
		}
		if (eventType == EventType.ON_PROGRESS) {
	   		onProgress(eventMessage, eventData);
		}
		else if (eventType >= EventType.ON_CUSTOM) {
	   		onCustomEvent(eventType, eventMessage, eventData);
		}
		else if (eventType == EventType.ON_SUCCESS) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onSuccess(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == EventType.ON_CANCEL) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onCancel(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == EventType.ON_ERROR) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onError(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
	}

	private void logEventMessage(int eventType, String eventMessage, Object eventData) {
		StringBuffer messageSb = new StringBuffer();
		messageSb.append("[").append(name).append("]");
		messageSb.append("[").append(EventType.getEventTypeName(eventType)).append("]");
		if (eventMessage != null) {
			messageSb.append(" message: " + eventMessage);
		}
		if (eventData != null) {
			if (eventData instanceof Throwable) {
				Throwable throwable = (Throwable)eventData;
				messageSb.append(" data: " + (throwable.getMessage() == null ? throwable.toString() : throwable.getMessage()));
			}
			else {
				messageSb.append(" data: " + eventData);
			}
		}
		logger.debug(messageSb.toString());
	}

	public void onStart(String eventMessage, Object eventData) {
	
	}

	public void onProgress(String eventMessage, Object eventData) {

	}

	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {

	}

	public void onBeforeCompletion(int eventType, String eventMessage, Object eventData) {

	}

	public void onSuccess(String eventMessage, Object eventData) {

	}

	public void onError(String eventMessage, Object eventData) {

	}

	public void onCancel(String eventMessage, Object eventData) {

	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {

	}

	public static Logger getLogger() {
		return logger;
	}

}
