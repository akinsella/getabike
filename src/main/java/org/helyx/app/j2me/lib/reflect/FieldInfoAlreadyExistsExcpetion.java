package org.helyx.app.j2me.lib.reflect;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class FieldInfoAlreadyExistsExcpetion extends NestedRuntimeException {

	public FieldInfoAlreadyExistsExcpetion() {
		super();
	}

	public FieldInfoAlreadyExistsExcpetion(String message) {
		super(message);
	}

	public FieldInfoAlreadyExistsExcpetion(Throwable throwable) {
		super(throwable);
	}

	public FieldInfoAlreadyExistsExcpetion(String message, Throwable throwable) {
		super(message, throwable);
	}

}
