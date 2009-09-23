package org.helyx.app.j2me.velocite.data.app.exception;

import org.helyx.helyx4me.exception.NestedException;

public class VersionException extends NestedException {

	public VersionException() {
		super();
	}

	public VersionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public VersionException(String message) {
		super(message);
	}

	public VersionException(Throwable throwable) {
		super(throwable);
	}

}
