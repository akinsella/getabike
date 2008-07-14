package org.helyx.app.j2me.velocite.data.carto.manager;

public class CartoManagerException extends Exception {

	public CartoManagerException() {
		super();
	}

	public CartoManagerException(String s) {
		super(s);
	}

	public CartoManagerException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}
	
	
}
