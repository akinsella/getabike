package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public 	class EventTypeException extends NestedRuntimeException {

	public EventTypeException() {
		super();
	}

	public EventTypeException(String message) {
		super(message);
	}

	public EventTypeException(Throwable throwable) {
		super(throwable);
	}

	public EventTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
