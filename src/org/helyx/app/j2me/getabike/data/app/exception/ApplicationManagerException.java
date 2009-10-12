package org.helyx.app.j2me.getabike.data.app.exception;

import org.helyx.helyx4me.exception.NestedRuntimeException;

public class ApplicationManagerException extends NestedRuntimeException {

	public ApplicationManagerException() {
		super();
	}

	public ApplicationManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ApplicationManagerException(String message) {
		super(message);
	}

	public ApplicationManagerException(Throwable throwable) {
		super(throwable);
	}

}
