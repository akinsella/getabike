package org.helyx.app.j2me.lib.util;

public class ErrorUtil {

	private ErrorUtil() {
		super();
	}
	
	public static String getMessage(Object object) {
		
		if (object == null) {
			return "null";
		}
		if (object instanceof Throwable) {
			Throwable throwable = (Throwable)object;
			return throwable.getMessage() == null ? throwable.toString() : throwable.getMessage();
		}
		
		return object.toString();
	}
	
}
