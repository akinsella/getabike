package org.helyx.app.j2me.lib.task;


public interface ProgressListener {

	void onEvent(int eventType, String eventMessage, Object eventData);
	
}
