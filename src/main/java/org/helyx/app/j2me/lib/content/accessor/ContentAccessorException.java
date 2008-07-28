package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.exception.NestedException;


public class ContentAccessorException extends NestedException {

	public ContentAccessorException() {
		super();
	}

	public ContentAccessorException(String message) {
		super(message);
	}

	public ContentAccessorException(Throwable throwable) {
		super(throwable);
	}

	public ContentAccessorException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
