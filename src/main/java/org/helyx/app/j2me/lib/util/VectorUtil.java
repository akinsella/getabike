package org.helyx.app.j2me.lib.util;

import java.util.Vector;

public class VectorUtil {
	
	private static final String CAT = "VECTOR_UTIL";

	private VectorUtil() {
		super();
	}
	
	public static void addElementsToVector(Vector vector, Object[] elements) {
		int length = elements.length;
		for (int i = 0 ; i < length ; i++) {
			Object element = elements[i];
			vector.addElement(element);
		}

	}
	
}
