package org.helyx.app.j2me.lib.content.accessor;


public class ContentAccessorException extends Exception {

	public ContentAccessorException() {
		super();
	}

	public ContentAccessorException(String message) {
		super(message);
	}

	public ContentAccessorException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}


}
