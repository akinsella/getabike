/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.util;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Shade;
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.WordWrapUtil;
import org.helyx.logging4me.Logger;



public final class GraphicsUtil {

	private static final Logger logger = Logger.getLogger("GRAPHICS_UTIL");

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
//				logger.info(ColorUtil.intToColor(color));
				g.setColor(color);
				g.drawLine(areaX + i, areaY, areaX + i, areaY + areaHeight - 1);
			}
		}
		else {

			for(int i = 0 ; i < areaHeight ; i++) {
				int color = ColorUtil.colorToInt(
					ColorUtil.getShadeComposant(srcRed, destRed, i, areaHeight),
					ColorUtil.getShadeComposant(srcGreen, destGreen, i, areaHeight),
					ColorUtil.getShadeComposant(srcBlue, destBlue, i, areaHeight)
				);
//				logger.info(ColorUtil.intToColor(color));
				g.setColor(color);
				g.drawLine(areaX, areaY + i, areaX + areaWidth - 1, areaY + i);
			}
		}
	} 

}