package org.helyx.app.j2me.lib.content.provider.exception;

import org.helyx.app.j2me.lib.exception.NestedException;

public class ContentProviderFactoryNotFoundExcepton extends NestedException {

	public ContentProviderFactoryNotFoundExcepton() {
		super();
	}

	public ContentProviderFactoryNotFoundExcepton(String message) {
		super(message);
	}

	public ContentProviderFactoryNotFoundExcepton(Throwable throwable) {
		super(throwable);
	}

	public ContentProviderFactoryNotFoundExcepton(String message, Throwable throwable) {
		super(message, throwable);
	}

}
