package org.helyx.app.j2me.getabike.data.carto.manager;

import org.helyx.helyx4me.exception.NestedException;

public class CartoManagerException extends NestedException {

	public CartoManagerException() {
		super();
	}

	public CartoManagerException(String message) {
		super(message);
	}

	public CartoManagerException(Throwable throwable) {
		super(throwable);
	}

	public CartoManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
