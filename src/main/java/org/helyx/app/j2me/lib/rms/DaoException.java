package org.helyx.app.j2me.lib.rms;

public class DaoException extends RuntimeException {

	public DaoException() {
		super();
	}

	public DaoException(String s) {
		super(s);
	}
	public DaoException(Exception e) {
		super(e.getMessage());
	}

}
