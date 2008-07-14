package org.helyx.app.j2me.lib.rms.exception;

public class MultiRecordDaoException extends RuntimeException {

	public MultiRecordDaoException() {
		super();
	}

	public MultiRecordDaoException(String s) {
		super(s);
	}
	public MultiRecordDaoException(Exception e) {
		super(e.getMessage());
	}

}
