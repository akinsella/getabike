package org.helyx.app.j2me.lib.content.provider.exception;

import org.helyx.app.j2me.lib.exception.NestedException;


public class ContentProviderException extends NestedException {

	public ContentProviderException() {
		super();
	}

	public ContentProviderException(String message) {
		super(message);
	}

	public ContentProviderException(Throwable throwable) {
		super(throwable);
	}

	public ContentProviderException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
