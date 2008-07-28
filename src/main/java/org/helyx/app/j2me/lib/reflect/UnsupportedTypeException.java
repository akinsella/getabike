package org.helyx.app.j2me.lib.reflect;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class UnsupportedTypeException extends NestedRuntimeException {

	public UnsupportedTypeException() {
		super();
	}

	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(Throwable throwable) {
		super(throwable);
	}

	public UnsupportedTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
