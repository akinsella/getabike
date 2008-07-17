package org.helyx.app.j2me.velocite.data.carto.provider.factory;

public class StationContentProviderFactoryNoFoundExcepton extends Exception {

	public StationContentProviderFactoryNoFoundExcepton() {
		super();
	}

	public StationContentProviderFactoryNoFoundExcepton(String message) {
		super(message);
	}

	public StationContentProviderFactoryNoFoundExcepton(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}

}
