package org.helyx.app.j2me.lib.rms.exception;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class BasicRecordDaoException extends NestedRuntimeException {

	public BasicRecordDaoException() {
		super();
	}

	public BasicRecordDaoException(String message) {
		super(message);
	}

	public BasicRecordDaoException(Throwable throwable) {
		super(throwable);
	}

	public BasicRecordDaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
