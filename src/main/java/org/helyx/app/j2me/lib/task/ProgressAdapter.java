package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.log.Log;

public class ProgressAdapter implements ProgressListener {

	protected static String CAT = "PROGRESS_ADAPTER";
	
	private String eventName;
	private String catName;
	private String progressAdapterCat;
	
	public ProgressAdapter(String progressAdapterCat) {
		super();
		this.progressAdapterCat = progressAdapterCat;
	}
	
	protected String getCat() {
//		if (catName == null) {
			catName = CAT + "[" + progressAdapterCat + "[" + eventName + "]]";
//		}
		return catName;
	}
	
	public void onEvent(int eventType, String eventMessage, Object eventData) {
		logEventMessage(eventType, eventMessage, eventData);
		if (eventType == ProgressEventType.ON_START) {
			eventName = ProgressEventType.ON_START_STR;
	   		onStart(eventMessage, eventData);
		}
		if (eventType == ProgressEventType.ON_PROGRESS) {
			eventName = ProgressEventType.ON_PROGRESS_STR;
	   		onProgress(eventMessage, eventData);
		}
		else if (eventType >= ProgressEventType.ON_CUSTOM) {
			eventName = ProgressEventType.ON_CUSTOM_STR + "[" + eventType + "]";
	   		onCustomEvent(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_SUCCESS) {
			eventName = ProgressEventType.ON_SUCCESS_STR;
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onSuccess(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_CANCEL) {
			eventName = ProgressEventType.ON_CANCEL_STR;
			onBeforeCompletion(eventType, eventMessage, eventData);
	   		onCancel(eventMessage, eventData);
	   		onAfterCompletion(eventType, eventMessage, eventData);
		}
		else if (eventType == ProgressEventType.ON_ERROR) {
			eventName = ProgressEventType.ON_ERROR_STR;
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
		Log.debug(getCat(), messageSb.toString());
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

}
