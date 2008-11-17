package org.helyx.app.j2me.lib.content.provider.exception;

import org.helyx.app.j2me.lib.exception.NestedException;


public class ContentProviderFactoryException extends NestedException {

	public ContentProviderFactoryException() {
		super();
	}

	public ContentProviderFactoryException(String message) {
		super(message);
	}

	public ContentProviderFactoryException(Throwable throwable) {
		super(throwable);
	}

	public ContentProviderFactoryException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
