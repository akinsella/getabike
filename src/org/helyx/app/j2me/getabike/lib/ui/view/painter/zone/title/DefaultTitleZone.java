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
package org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.title;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Shade;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.AbstractViewCanvasZone;
import org.helyx.logging4me.Logger;


public class DefaultTitleZone extends AbstractViewCanvasZone {

	private static final Logger logger = Logger.getLogger("DEFAULT_TITLE_ZONE");
	
	public DefaultTitleZone(AbstractView view) {
		super(view);
	}


	public Rectangle computeArea() {
		int padding = view.getTheme().getInt(ThemeConstants.WIDGET_TITLE_PADDING);
		return new Rectangle(0, 0, view.getViewCanvas().getWidth(), view.shouldPaintTitle() ? padding + FontUtil.MEDIUM_BOLD.getHeight() + padding : 0);
	}

	public void paintArea(Graphics graphics) {
//		int padding = view.getTheme().getInt(ThemeConstants.WIDGET_TITLE_PADDING);

		Rectangle titleArea = computeArea();
		Color shadeColor1 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		Color shadeColor2 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		GraphicsUtil.fillShade(graphics, titleArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleLineColor = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_LINE);
		graphics.setColor(titleLineColor.intValue());
		graphics.drawLine(titleArea.location.x, titleArea.location.y + titleArea.size.height, titleArea.location.x + titleArea.size.width, titleArea.location.y + titleArea.size.height);

		graphics.setFont(FontUtil.MEDIUM_BOLD);
		Color titleFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.drawString(view.renderTitle(), titleArea.location.x + titleArea.size.width / 2, titleArea.location.y + (titleArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
	}
	
}
