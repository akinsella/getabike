package org.helyx.app.j2me.lib.serializer;

import org.helyx.app.j2me.lib.exception.NestedException;

public class SerializerException extends NestedException {

	public SerializerException() {
		super();
	}

	public SerializerException(String message) {
		super(message);
	}

	public SerializerException(Throwable throwable) {
		super(throwable);
	}

	public SerializerException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
