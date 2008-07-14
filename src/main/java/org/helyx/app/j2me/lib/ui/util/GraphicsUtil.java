package org.helyx.app.j2me.lib.ui.util;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Shade;


public final class GraphicsUtil {

	private static final String CAT = "GRAPHICS_UTIL";

	public static void drawStringWordWrap(Graphics g, String text, int x, int y, int fullWidth, int style) {
		WordWrapUtil.paint(g, text, x, y, fullWidth, style);
	}

	public static void fillShade(Graphics g, Rectangle area, Shade shade, boolean horizontal) {
		int areaX = area.location.x;
		int areaY = area.location.y;
		int areaWidth = area.size.width;
		int areaHeight = area.size.height;
		
		int srcRed = shade.src.red;
		int srcGreen = shade.src.green;
		int srcBlue = shade.src.blue;
		
		int destRed = shade.dest.red;
		int destGreen = shade.dest.green;
		int destBlue = shade.dest.blue;

		if (horizontal) {

			for(int i = 0 ; i < areaWidth ; i++) {
				int color = ColorUtil.colorToInt(
					ColorUtil.getShadeComposant(srcRed, destRed, i, areaWidth),
					ColorUtil.getShadeComposant(srcGreen, destGreen, i, areaWidth),
					ColorUtil.getShadeComposant(srcBlue, destBlue, i, areaWidth)
				);
//				Log.info(CAT, ColorUtil.intToColor(color));
				g.setColor(color);
				g.drawLine(areaX + i, areaY, areaX + i, areaY + areaHeight);
			}
		}
		else {

			for(int i = 0 ; i < areaHeight ; i++) {
				int color = ColorUtil.colorToInt(
					ColorUtil.getShadeComposant(srcRed, destRed, i, areaHeight),
					ColorUtil.getShadeComposant(srcGreen, destGreen, i, areaHeight),
					ColorUtil.getShadeComposant(srcBlue, destBlue, i, areaHeight)
				);
//				Log.info(CAT, ColorUtil.intToColor(color));
				g.setColor(color);
				g.drawLine(areaX, areaY + i, areaX + areaWidth, areaY + i);
			}
		}
	} 

}