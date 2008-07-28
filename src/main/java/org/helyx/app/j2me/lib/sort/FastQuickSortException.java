package org.helyx.app.j2me.lib.sort;

import org.helyx.app.j2me.lib.exception.NestedRuntimeException;

public class FastQuickSortException extends NestedRuntimeException {

	public FastQuickSortException() {
		super();
	}

	public FastQuickSortException(String message) {
		super(message);
	}

	public FastQuickSortException(Throwable throwable) {
		super(throwable);
	}

	public FastQuickSortException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
