package org.helyx.app.j2me.getabike.data.carto.service;

import org.helyx.app.j2me.getabike.lib.exception.NestedException;

public class StationNotFoundException extends NestedException {

	public StationNotFoundException() {
		super();
	}

	public StationNotFoundException(String message) {
		super(message);
	}

	public StationNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public StationNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
