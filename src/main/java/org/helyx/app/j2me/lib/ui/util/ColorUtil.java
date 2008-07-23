package org.helyx.app.j2me.lib.ui.util;

import org.helyx.app.j2me.lib.ui.graphics.Color;

public class ColorUtil {

	public static final int WHITE = 0xFFFFFF;
	public static final int BLACK = 0x000000;
	public static final int RED = 0xCC0000;
	public static final int DARK_GREY = 0x404040;
	public static final int LIGHT_GREY = 0xE0E0E0;
	public static final int GREY = 0x909090;
	public static final int DARK_RED = 0x990000;

	private ColorUtil() {
		super();
	}
	
	public static Color intToColor(int intColor) {
		
		Color color = new Color();
		
		color.blue = intColor & 0xFF;
		color.green = (intColor >> 8) & 0xFF;
		color.red = (intColor >> 16) & 0xFF;
		
		return color;
	}
	
	public static int colorToInt(Color color) {
		
		int intColor = color.red << 16 | color.green << 8 | color.blue;
		
		return intColor;
	}
	
	public static int colorToInt(int red, int green, int blue) {
		
		int intColor = red << 16 | green << 8 | blue;
		
		return intColor;
	}
	
	public static int getShadeComposant(int srcComposant, int destComposant, int percentile, int maxPercentile) {
		return (int)((double)(srcComposant * (maxPercentile - percentile) + destComposant * percentile) / (double)maxPercentile);
	}
	
}
