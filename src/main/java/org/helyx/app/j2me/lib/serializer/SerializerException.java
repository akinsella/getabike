package org.helyx.app.j2me.lib.serializer;

public class SerializerException extends Exception {

	public SerializerException() {
		super();
	}

	public SerializerException(String message) {
		super(message);
	}

	public SerializerException(Throwable t) {
		super(t == null ? null : t.getMessage());
	}

}
