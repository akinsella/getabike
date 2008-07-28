package org.helyx.app.j2me.lib.util;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class EmptyStackException extends NestedRuntimeException {

	public EmptyStackException() {
		super();
	}

	public EmptyStackException(String message) {
		super(message);
	}

	public EmptyStackException(Throwable throwable) {
		super(throwable);
	}

	public EmptyStackException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
