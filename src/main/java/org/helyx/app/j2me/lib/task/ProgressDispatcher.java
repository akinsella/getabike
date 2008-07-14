package org.helyx.app.j2me.lib.task;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;

public class ProgressDispatcher implements IProgressDispatcher {
	
	private static final String CAT = "PROGRESS_DISPATCHER";
	
	private String name;
	
	private Vector progressListenerList = new Vector();
	
	public ProgressDispatcher() {
		super();
	}
	
	private String getLoggerName() {
		if (name == null) {
			return CAT;
		}
		return CAT + "[" + name + "]";
	}
	

	public void addProgressListener(ProgressListener progressListener) {
		progressListenerList.addElement(progressListener);
	}

	public void removeProgressListener(ProgressListener progressListener) {
		progressListenerList.removeElement(progressListener);
	}
	
	public void fireEvent(int eventType) {
		fireEvent(eventType, null, null);
	}

	public void fireEvent(int eventType, String eventMessage) {
		fireEvent(eventType, eventMessage, null);
	}

	public void fireEvent(int eventType, Object eventData) {
		fireEvent(eventType, null, eventData);
	}
		
	public void fireEvent(int eventType, String eventMessage, Object eventData) {
		
		Log.debug(getLoggerName(), "[" + name + "] eventType=" + eventType + ", eventMessage=" + eventMessage + ", eventData=" + eventData);
		int size = progressListenerList.size();
		
		for (int i = 0 ; i < size ; i++) {
			try {
				ProgressListener progressListener = (ProgressListener)progressListenerList.elementAt(i);
				progressListener.onEvent(eventType, eventMessage, eventData);
			}
			catch(Throwable t) {
				Log.warn(getLoggerName(), t);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
