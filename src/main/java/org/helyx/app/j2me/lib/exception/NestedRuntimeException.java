package org.helyx.app.j2me.lib.exception;

public class NestedRuntimeException extends RuntimeException {

	private Throwable throwable;

	public NestedRuntimeException() {
		super();
	}

	public NestedRuntimeException(String message) {
		super(message);
	}

	public NestedRuntimeException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
		this.throwable = throwable;
	}
	
	public NestedRuntimeException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	public Throwable getCause() {
		return throwable;
	}

}
