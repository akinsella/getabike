package org.helyx.app.j2me.lib.task;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public 	class ProgressEventTypeException extends NestedRuntimeException {

	public ProgressEventTypeException() {
		super();
	}

	public ProgressEventTypeException(String message) {
		super(message);
	}

	public ProgressEventTypeException(Throwable throwable) {
		super(throwable);
	}

	public ProgressEventTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
