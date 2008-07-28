package org.helyx.app.j2me.lib.reflect;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class InvalidFieldException extends NestedRuntimeException {

	public InvalidFieldException() {
		super();
	}

	public InvalidFieldException(String message) {
		super(message);
	}

	public InvalidFieldException(Throwable throwable) {
		super(throwable);
	}

	public InvalidFieldException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
