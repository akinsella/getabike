package org.helyx.app.j2me.lib.util;

public class EmptyStackException extends RuntimeException {

	public EmptyStackException() {
		super();
	}

	public EmptyStackException(String message) {
		super(message);
	}

	public EmptyStackException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}

}
