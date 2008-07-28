package org.helyx.app.j2me.velocite.data.city.manager;

import org.helyx.app.j2me.lib.exception.NestedException;

public class CityManagerException extends NestedException {

	public CityManagerException() {
		super();
	}

	public CityManagerException(String message) {
		super(message);
	}

	public CityManagerException(Throwable throwable) {
		super(throwable);
	}

	public CityManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
