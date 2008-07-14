package org.helyx.app.j2me.lib.ui.util;

public class KeyMap {

	public int softKeyLeft;
	public int softKeyRight;
	
	public KeyMap() {
		super();
	}
	
	public KeyMap(int softKeyLeft, int softKeyRight) {
		super();
		
		this.softKeyLeft = softKeyLeft;
		this.softKeyRight = softKeyRight;
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer("[KeyMap]");
		sb.append(" softKeyLeft=" + softKeyLeft);
		sb.append(", softKeyRight=" + softKeyRight);
		
		return sb.toString();
	}

}
