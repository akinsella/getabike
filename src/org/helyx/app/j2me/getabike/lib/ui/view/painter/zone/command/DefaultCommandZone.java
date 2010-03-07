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
package org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Shade;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.zone.command.AbstractCommandZone;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class DefaultCommandZone extends AbstractCommandZone {

	private static final Logger logger = Logger.getLogger("DEFAULT_MENU_ZONE");

	public DefaultCommandZone(AbstractView view) {
		super(view);
	}

	public Rectangle computeArea() {
		int padding = view.getTheme().getInt(ThemeConstants.WIDGET_COMMAND_PADDING);
		int commandZoneHeight = (view.shouldPaintCommand() ? FontUtil.SMALL_BOLD.getHeight() + 2 + padding * 2 : 0);
		return new Rectangle(0, view.getViewCanvas().getHeight() - commandZoneHeight, view.getViewCanvas().getWidth(), commandZoneHeight);
	}

	public void paintArea(Graphics graphics) {

		Rectangle zoneRect = computeArea();
		
		Color shadeColor1 = view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_DARK);
		Color shadeColor2 = view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_LIGHT);
		GraphicsUtil.fillShade(graphics, zoneRect, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_LINE).intValue());		
		graphics.drawLine(zoneRect.location.x, zoneRect.location.y, zoneRect.location.x + zoneRect.size.width, zoneRect.location.y);

		graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_LINE_2).intValue());		
		graphics.drawLine(zoneRect.location.x, zoneRect.location.y + 1, zoneRect.location.x + zoneRect.size.width, zoneRect.location.y + 1);

		int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
		
		if (view.getThirdCommand() != null) {
			Rectangle commandArea = computeThirdCommandArea(zoneRect);
			if (currentCommandPressed == view.getThirdCommand() && view.isThirdCommandEnabled()) {
				Shade shade = new Shade(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_LIGHT_SELECTED), view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_DARK_SELECTED));
				GraphicsUtil.fillShade(graphics, commandArea, shade, false);
				graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_LINE).intValue());
				graphics.drawLine(commandArea.location.x + commandArea.size.width, commandArea.location.y, commandArea.location.x + commandArea.size.width, commandArea.location.y + commandArea.size.height);
				graphics.drawLine(commandArea.location.x + commandArea.size.width, commandArea.location.y, commandArea.location.x + commandArea.size.width, commandArea.location.y + commandArea.size.height);
			}
			graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_FONT).intValue());
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString(view.getThirdCommand().renderText(), commandArea.location.x + commandArea.size.width / 2, commandArea.location.y + (commandArea.size.height - smallBoldFontHeight) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (view.getPrimaryCommand() != null) {
			Rectangle commandArea = computePrimaryCommandArea(zoneRect);
			if (currentCommandPressed == view.getPrimaryCommand() && view.isPrimaryCommandEnabled()) {
				Shade shade = new Shade(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_LIGHT_SELECTED), view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_DARK_SELECTED));
				GraphicsUtil.fillShade(graphics, commandArea, shade, false);
				graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_LINE).intValue());
				graphics.drawLine(commandArea.location.x, commandArea.location.y, commandArea.location.x, commandArea.location.y + commandArea.size.height);
				graphics.drawLine(commandArea.location.x + commandArea.size.width, commandArea.location.y, commandArea.location.x + commandArea.size.width, commandArea.location.y + commandArea.size.height);
			}
			graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_FONT).intValue());
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString(view.getPrimaryCommand().renderText(), commandArea.location.x + commandArea.size.width / 2, commandArea.location.y + (commandArea.size.height - smallBoldFontHeight) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (view.getSecondaryCommand() != null) {
			Rectangle commandArea = computeSecondaryCommandArea(zoneRect);
			if (currentCommandPressed == view.getSecondaryCommand() && view.isSecondaryCommandEnabled()) {
				Shade shade = new Shade(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_LIGHT_SELECTED), view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_BG_SHADE_DARK_SELECTED));
				GraphicsUtil.fillShade(graphics, commandArea, shade, false);
				graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_LINE).intValue());
				graphics.drawLine(commandArea.location.x, commandArea.location.y, commandArea.location.x, commandArea.location.y + commandArea.size.height);
				graphics.drawLine(commandArea.location.x + commandArea.size.width, commandArea.location.y, commandArea.location.x + commandArea.size.width, commandArea.location.y + commandArea.size.height);
			}
			graphics.setColor(view.getTheme().getColor(ThemeConstants.WIDGET_COMMAND_FONT).intValue());
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString(view.getSecondaryCommand().renderText(), commandArea.location.x + commandArea.size.width / 2, commandArea.location.y + (commandArea.size.height - smallBoldFontHeight) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

	}
	
	public Rectangle computeCommandArea(Rectangle zoneRect, Command command) {
		if (command == null) {
			return null;
		}

		int padding = view.getTheme().getInt(ThemeConstants.WIDGET_COMMAND_PADDING);
		if (logger.isDebugEnabled()) {
			logger.debug("Padding: " + padding);
		}
		
		Rectangle commandArea = null;
		
		if (command == view.getThirdCommand()) {
			int commandWidth = FontUtil.SMALL_BOLD.stringWidth(view.getThirdCommand().renderText()) + padding * 2;
			if (logger.isDebugEnabled()) {
				logger.debug("Third commandWidth: " + commandWidth);
			}
			commandArea = new Rectangle(zoneRect.location.x, zoneRect.location.y + 2, commandWidth, zoneRect.size.height - 2);
		}
		else if (command == view.getPrimaryCommand()) {
			int commandWidth = FontUtil.SMALL_BOLD.stringWidth(view.getPrimaryCommand().renderText()) + padding * 2;
			if (logger.isDebugEnabled()) {
				logger.debug("Primary commandWidth: " + commandWidth);
			}
			commandArea = new Rectangle(zoneRect.location.x + (zoneRect.size.width - commandWidth) / 2, zoneRect.location.y + 2, commandWidth, zoneRect.size.height - 2);
		}
		else if (command == view.getSecondaryCommand()) {
			int commandWidth = FontUtil.SMALL_BOLD.stringWidth(view.getSecondaryCommand().renderText()) + padding * 2;
			if (logger.isDebugEnabled()) {
				logger.debug("Secondary commandWidth: " + commandWidth);
			}
			commandArea = new Rectangle(zoneRect.location.x + zoneRect.size.width - commandWidth, zoneRect.location.y + 2, commandWidth, zoneRect.size.height - 2);
		}
		else {
			throw new RuntimeException("Wrong command passed as argument");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Command Area: " + commandArea.toString());
		}
		
		return commandArea;
	}
	
}
