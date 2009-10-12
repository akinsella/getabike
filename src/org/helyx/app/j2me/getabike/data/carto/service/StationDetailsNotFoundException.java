package org.helyx.app.j2me.getabike.data.carto.service;

import org.helyx.helyx4me.exception.NestedException;

public class StationDetailsNotFoundException extends NestedException {

	public StationDetailsNotFoundException() {
		super();
	}

	public StationDetailsNotFoundException(String message) {
		super(message);
	}

	public StationDetailsNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public StationDetailsNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
