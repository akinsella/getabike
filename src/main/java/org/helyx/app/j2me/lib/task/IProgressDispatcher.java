package org.helyx.app.j2me.lib.task;

public interface IProgressDispatcher extends IProgressListenerManager {

	String getName();

	void setName(String name);

	void fireEvent(int eventType);
	
	void fireEvent(int eventType, String eventMessage);
	
	void fireEvent(int eventType, Object eventData);
		
	void fireEvent(int eventType, String eventMessage, Object eventData);

}
