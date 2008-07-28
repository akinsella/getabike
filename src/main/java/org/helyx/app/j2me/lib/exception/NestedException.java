package org.helyx.app.j2me.lib.exception;

public class NestedException extends Exception {

	private Throwable throwable;
	
	public NestedException() {
		super();
	}

	public NestedException(String message) {
		super(message);
	}

	public NestedException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
		this.throwable = throwable;
	}

	public NestedException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}
	
	public Throwable getCause() {
		return throwable;
	}

}
