package org.helyx.app.j2me.getabike.data.language.manager;

import org.helyx.helyx4me.exception.NestedException;

public class LanguageManagerException extends NestedException {

	public LanguageManagerException() {
		super();
	}

	public LanguageManagerException(String message) {
		super(message);
	}

	public LanguageManagerException(Throwable throwable) {
		super(throwable);
	}

	public LanguageManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
