package org.helyx.app.j2me.lib.ui.util;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class DialogUtil {

	private DialogUtil() {
		super();
	}
	
	public static void showAlertMessage(MIDlet midlet, Displayable displayable, String title, String message) {
		showAlertMessage(midlet, displayable, title, message, Alert.FOREVER);
	}
	
	public static void showAlertMessage(MIDlet midlet, Displayable displayable, String title, String message, int timeout) {
		Alert alert;
		
		alert = new Alert(title);
		alert.setString(message);
		alert.setTimeout(timeout);
		Display.getDisplay(midlet).setCurrent(alert, displayable);
	}

}
