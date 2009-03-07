package org.helyx.app.j2me.lib.concurrent;


public class FutureTimeoutException extends FutureException {

	public FutureTimeoutException() {
		super();
	}

	public FutureTimeoutException(String message) {
		super(message);
	}
	
	public FutureTimeoutException(Throwable throwable) {
		super(throwable);
	}

}
