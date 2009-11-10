package org.helyx.app.j2me.getabike.util;

import java.io.IOException;

import javax.microedition.io.ConnectionNotFoundException;

import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.stream.exception.HttpAccessException;

public class ErrorManager {

	private ErrorManager() {
		super();
	}
	
	public static String getErrorMessage(AbstractMIDlet midlet, Throwable t) {
		
		if (t == null) {
			return midlet.getMessage("dialog.error.unexpected");
		}
		
		Throwable rootCause = org.helyx.helyx4me.util.ErrorUtil.getRootCause(t);
		
		if (rootCause.getMessage() == null) {
			return midlet.getMessage("dialog.error.unexpected");			
		}
		
		if (rootCause instanceof SecurityException) {
			return midlet.getMessage("security.api.access.error.message");
		}
		else if (rootCause instanceof ConnectionNotFoundException) {
			return midlet.getMessage("connection.not.found");
		}
		else if (rootCause instanceof IOException) {
			return midlet.getMessage("io.exception.message");
		}
		else if (rootCause instanceof HttpAccessException) {
			HttpAccessException hae = (HttpAccessException)rootCause;
			int returnCode = hae.getReturnCode();
			String errorMessage = midlet.getMessage("http.exception.message") + " - " + returnCode;
			String secondErrorMsg = null;
			if (returnCode >= 400 && returnCode <= 404 || returnCode >= 500 && returnCode <= 505) {
				secondErrorMsg = midlet.getMessage("http.code." + returnCode + ".msg");
			}
			if (secondErrorMsg != null) {
				errorMessage += " - " + secondErrorMsg;
			}
		}

		return t.getMessage();
	}
	
}
