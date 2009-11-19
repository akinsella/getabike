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
package org.helyx.app.j2me.getabike.ui.view.renderer;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.graphics.Shade;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.ColorUtil;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.util.GraphicsUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.support.list.ICellRenderer;
import org.helyx.helyx4me.ui.view.support.menu.MenuListView;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class MenuItemRenderer implements ICellRenderer {

	private Logger logger = Logger.getLogger("STATION_ITEM_RENDERER");
	
	public MenuItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
		MenuListView menuListView = (MenuListView)view;
		Menu menu = menuListView.getMenu();
    	MenuItem menuItem = (MenuItem)menu.getMenuItem(offset);

    	boolean isSelected = menuListView.isItemSelected(offset);

        Font font = isSelected ? FontUtil.MEDIUM_BOLD : FontUtil.SMALL;

	    Color menuItemColor;
	    boolean isMenuEnabled = menuItem.isEnabled();
	    Rectangle bRect = itemClientArea;

	    if (isSelected) {
	    	menuItemColor = menuListView.getTheme().getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT_SELECTED : ThemeConstants.WIDGET_MENU_FONT_SELECTED_DISABLED);
	    }
	    else {
	    	menuItemColor = menuListView.getTheme().getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT : ThemeConstants.WIDGET_MENU_FONT_DISABLED);
	    }
	    
    	g.setColor(ColorUtil.DARK_GREY);
    	Rectangle  bRect2 = new Rectangle(bRect.location.x + 5 + 1, bRect.location.y + 2 + 1, bRect.size.width - 10 - 1, bRect.size.height - 4 - 1);
    	Shade shade = !isSelected ?
    		new Shade(menuListView.getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_DARK), menuListView.getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_LIGHT)) :
    		new Shade(menuListView.getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_DARK_SELECTED), menuListView.getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_LIGHT_SELECTED));
    	GraphicsUtil.fillShade(g, bRect2, shade, true);
    	g.setColor(ColorUtil.DARK_GREY);
    	g.drawLine(bRect.location.x + 5 + 1, bRect.location.y + 2, bRect.location.x + 5 + bRect.size.width - 10 - 1, bRect.location.y + 2);
	    	g.drawLine(bRect.location.x + 5 + 1, bRect.location.y + 2 + bRect.size.height - 4, bRect.location.x + 5 + bRect.size.width - 10 - 1, bRect.location.y + 2 + bRect.size.height - 4);
	    	g.drawLine(bRect.location.x + 5, bRect.location.y + 2 + 1, bRect.location.x + 5, bRect.location.y + 2 + bRect.size.height - 4 - 1);
	    	g.drawLine(bRect.location.x + 5 + bRect.size.width - 10, bRect.location.y + 2 + 1, bRect.location.x + 5 + bRect.size.width - 10, bRect.location.y + 2 + bRect.size.height - 4 - 1);
	    	
	    g.setColor(menuItemColor.intValue());
        g.setFont(font);
        g.drawString(
        		menuListView.getMessage(menuItem.getText()), 
        		bRect.location.x + bRect.size.width / 2, 
        		bRect.location.y + bRect.size.height / 2 + (font.getHeight() - font.getBaselinePosition()), 
        		Graphics.HCENTER | Graphics.BASELINE);
	}

	public int computeHeight(AbstractListView view, Object itemObject, int offset) {
		int itemHeight = view.computeViewPortArea().size.height / 5;
        return itemHeight;
	}

}
