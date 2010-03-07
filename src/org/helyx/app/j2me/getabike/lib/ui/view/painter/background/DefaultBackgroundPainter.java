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
package org.helyx.app.j2me.getabike.lib.ui.view.painter.background;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.Painter;
import org.helyx.logging4me.Logger;


public class DefaultBackgroundPainter implements Painter {

	private static final Logger logger = Logger.getLogger("DEFAULT_BACKGROUND_ZONE");
	
	public DefaultBackgroundPainter() {
		super();
	}

	public void paintArea(Graphics graphics, AbstractView view, Rectangle rect) {
		Color bgColor = view.getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND);

        graphics.setColor(bgColor.intValue());
        graphics.fillRect(
        		rect.location.x,
        		rect.location.y, 
        		rect.size.width, 
        		rect.size.height);
	}
	
}
