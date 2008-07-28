package org.helyx.app.j2me.lib.concurrent;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class FutureException extends NestedRuntimeException {

	public FutureException() {
		super();
	}

	public FutureException(String message) {
		super(message);
	}
	
	public FutureException(Throwable throwable) {
		super(throwable);
	}

}
