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
package org.helyx.app.j2me.getabike.lib.test.list;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.ICellRenderer;
import org.helyx.logging4me.Logger;


public class StationItemRenderer implements ICellRenderer {

	private Logger logger = Logger.getLogger("STATION_ITEM_RENDERER");
	
	public StationItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		Station station = (Station)itemObject;
    	
    	boolean isSelected = view.isItemSelected(offset);
    	
     	if (isSelected) {
    		Color listFontSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
    		g.setColor(listFontSelectedColor.intValue());
    	}
    	else {
    		Color listFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
    		g.setColor(listFontColor.intValue());
    	}
        if (!station.open) {
    		Color listErrorColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_ERROR);
         	g.setColor(listErrorColor.intValue());
        }

        int x = itemClientArea.location.x;
        int y = itemClientArea.location.y;

        g.setFont(isSelected ? FontUtil.LARGE_BOLD : FontUtil.SMALL_BOLD);
        g.drawString(station.number + " - " + (station.name != null ? station.name : "Station sans nom"), x + 5, y + 2, Graphics.LEFT | Graphics.TOP);
        
        if (station.fullAddress != null) {
	    	if (isSelected) {
	    		Color listFontSecondSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
	    		g.setColor(listFontSecondSelectedColor.intValue());
	    	}
	    	else {
	    		Color listFontSecondColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
	    		g.setColor(listFontSecondColor.intValue());
	    	}
	        g.setFont(isSelected ? FontUtil.LARGE : FontUtil.SMALL);
	    	g.drawString(station.fullAddress, x + 5, y + 2 + (isSelected ? FontUtil.LARGE.getHeight() : FontUtil.SMALL.getHeight()), Graphics.LEFT | Graphics.TOP);
	    }
	}

	public int computeHeight(AbstractListView view, Object itemObject, int offset) {
    	
    	boolean isSelected = view.isItemSelected(offset);

    	int smallFontHeight = isSelected ? FontUtil.LARGE.getHeight() : FontUtil.SMALL.getHeight();
        int smallBoldFontHeight = isSelected ? FontUtil.LARGE_BOLD.getHeight() : FontUtil.SMALL_BOLD.getHeight();
        
        int itemHeight = 1 + smallFontHeight + 1 + smallBoldFontHeight + 1 + 1;
 	
        return itemHeight;
	}

}
