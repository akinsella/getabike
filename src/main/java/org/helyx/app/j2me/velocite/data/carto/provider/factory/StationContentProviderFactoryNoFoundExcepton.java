package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.exception.NestedException;

public class StationContentProviderFactoryNoFoundExcepton extends NestedException {

	public StationContentProviderFactoryNoFoundExcepton() {
		super();
	}

	public StationContentProviderFactoryNoFoundExcepton(String message) {
		super(message);
	}

	public StationContentProviderFactoryNoFoundExcepton(Throwable throwable) {
		super(throwable);
	}

	public StationContentProviderFactoryNoFoundExcepton(String message, Throwable throwable) {
		super(message, throwable);
	}

}
