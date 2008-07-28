package org.helyx.app.j2me.lib.rms.exception;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class MultiRecordDaoException extends NestedRuntimeException {

	public MultiRecordDaoException() {
		super();
	}

	public MultiRecordDaoException(String message) {
		super(message);
	}

	public MultiRecordDaoException(Throwable throwable) {
		super(throwable);
	}

	public MultiRecordDaoException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
