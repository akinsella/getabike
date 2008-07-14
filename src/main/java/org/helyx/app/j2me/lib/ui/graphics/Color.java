package org.helyx.app.j2me.lib.ui.graphics;

public class Color {

	public int red;
	public int green;
	public int blue;
	
	public Color() {
		super();
	}

	public Color(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
		
	public String toString() {
		StringBuffer sb = new StringBuffer("[Color] ")
		.append(" red=").append(red)
		.append(", green=").append(green)
		.append(", blue=").append(blue);

		return sb.toString();
	}
	
}
