package org.helyx.app.j2me.lib.ui.graphics;

import org.helyx.app.j2me.lib.ui.util.ColorUtil;



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

	public Color(int intColor) {
		super();
		ColorUtil.intToColor(this, intColor);
	}

	public Color(String colorValue) {
		super();
		if (colorValue.indexOf('#') == 0) {
			ColorUtil.parseHtmlColor(this, colorValue);
		}
		else {
			ColorUtil.parseHexaColor(this, colorValue);
		}
	}

	public int intValue() {
        return ColorUtil.colorToInt(this);
	}
		
	public String toString() {
		StringBuffer sb = new StringBuffer("[Color] ")
		.append(" red=").append(red)
		.append(", green=").append(green)
		.append(", blue=").append(blue);

		return sb.toString();
	}
	
}
