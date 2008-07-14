package org.helyx.app.j2me.lib.rms.exception;

public class BasicRecordDaoException extends RuntimeException {

	public BasicRecordDaoException() {
		super();
	}

	public BasicRecordDaoException(String s) {
		super(s);
	}
	public BasicRecordDaoException(Exception e) {
		super(e.getMessage());
	}

}
