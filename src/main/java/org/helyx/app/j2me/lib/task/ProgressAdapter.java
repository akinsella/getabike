package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ProgressAdapter implements ProgressListener {

	protected static Log log = LogFactory.getLog("PROGRESS_ADAPTER");
	
	public ProgressAdapter() {
		super();
	}
	
	public void onEvent(int eventType, String eventMessage, Object eventData) {
		
		logEventMessage(eventType, eventMessage, eventData);
		if (eventType == ProgressEventType.ON_START) {
	   		onStart(eventMessage, eventData);
		}
		if (eventType == ProgressEventType.ON_PROGRESS) {
	   		onProgress(eventMessage, eventData);
		}
		else if (eventType >= ProgressEventType.ON_CUSTOM) {
	   		onCustomEvent(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_SUCCESS) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onSuccess(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_CANCEL) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onCancel(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_ERROR) {
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onError(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
	}

	private void logEventMessage(int eventType, String eventMessage, Object eventData) {
		StringBuffer messageSb = new StringBuffer();
		messageSb.append("[").append(ProgressEventType.getEventTypeName(eventType)).append("]");
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
		log.debug(messageSb.toString());
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

	public static Log getLog() {
		return log;
	}

}
