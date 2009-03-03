package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.lib.ui.view.support.list.ICellRenderer;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class StationItemRenderer implements ICellRenderer {

	private Logger logger = LoggerFactory.getLogger("STATION_ITEM_RENDERER");
	
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
//        int width = itemClientArea.size.width;
//        int height = itemClientArea.size.height;

        g.setFont(FontUtil.SMALL_BOLD);
        g.drawString(station.number + " - " + station.name, x + 5, y + 2, Graphics.LEFT | Graphics.TOP);
 
    	if (isSelected) {
    		Color listFontSecondSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
    		g.setColor(listFontSecondSelectedColor.intValue());
    	}
    	else {
    		Color listFontSecondColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
    		g.setColor(listFontSecondColor.intValue());
    	}
        g.setFont(FontUtil.SMALL);
    	g.drawString(station.fullAddress, x + 5, y + 2 + FontUtil.SMALL_BOLD.getHeight(), Graphics.LEFT | Graphics.TOP);
		
	}

}
