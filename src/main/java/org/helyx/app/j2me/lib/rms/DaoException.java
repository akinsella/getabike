package org.helyx.app.j2me.lib.rms;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class DaoException extends NestedRuntimeException {

	public DaoException() {
		super();
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable throwable) {
		super(throwable);
	}

	public DaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
