package org.helyx.app.j2me.lib.sort;

public class FastQuickSortException extends RuntimeException {

	public FastQuickSortException() {
		super();
	}

	public FastQuickSortException(String message) {
		super(message);
	}

	public FastQuickSortException(Throwable t) {
		super(t == null ? null : t.getMessage());
	}

}
