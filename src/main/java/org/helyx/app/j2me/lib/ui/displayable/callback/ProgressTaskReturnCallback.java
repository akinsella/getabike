package org.helyx.app.j2me.lib.ui.displayable.callback;

import org.helyx.app.j2me.lib.task.Event;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.task.EventTypeException;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public class ProgressTaskReturnCallback implements IReturnCallback {

	public ProgressTaskReturnCallback() {
		super();
	}

	public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
		Event event = (Event)data;
		switch(event.getType()) {
			case EventType.ON_SUCCESS:
				onSuccess(currentDisplayable, event.getMessage(), event.getData());
				break;
			case EventType.ON_ERROR:
				onError(currentDisplayable, event.getMessage(), event.getData());
				break;
			case EventType.ON_CANCEL:
				onError(currentDisplayable, event.getMessage(), event.getData());
				break;
			default:
				throw new EventTypeException("Unsupported event type: '" + EventType.getEventTypeName(event.getType()) + "'");
		}
	}

	public void onCancel(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
		
	}

	public void onError(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {

	}

	public void onSuccess(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
	}

}
