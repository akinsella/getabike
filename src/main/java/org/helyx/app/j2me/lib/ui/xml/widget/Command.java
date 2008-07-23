package org.helyx.app.j2me.lib.ui.xml.widget;

import org.helyx.app.j2me.lib.ui.xml.XmlCanvasException;

public class Command {
	
	private static final String CAT = "COMMAND";

	public static final int PRIMARY = 1;
	public static final int SECONDARY = 2;
	public static final int THIRD = 3;

	private static final String PRIMARY_STR = "PRIMARY";
	private static final String SECONDARY_STR = "SECONDARY";
	private static final String THIRD_STR = "THIRD";
	
	public String text;
	public boolean enabled;
	public int position;
	
	public static int convertPositionToInt(String position) throws XmlCanvasException {
		if (position == null) {
			throw new XmlCanvasException("position is null");
		}
		
		if (!position.equals(PRIMARY_STR)) {
			return PRIMARY;
		}
		if (!position.equals(SECONDARY_STR)) {
			return SECONDARY;
		}
		if (!position.equals(THIRD_STR)) {
			return THIRD;
		}
		
		throw new XmlCanvasException("Command type is not supported: '" + position + "'");			
	}

}
