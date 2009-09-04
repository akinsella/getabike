package org.helyx.app.j2me.velocite.ui.view.renderer;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.support.list.ICellRenderer;
import org.helyx.logging4me.Logger;


public class StationItemRenderer implements ICellRenderer {

	private Logger logger = Logger.getLogger("STATION_ITEM_RENDERER");
	
	private static final int BASE_LEFT_POS = 5;
	private static final int PADDING_TOP = 2;
	private static final int LINE_SPACING = 0;
	
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

        g.setFont(FontUtil.SMALL_BOLD);
        
        int addToXPos = BASE_LEFT_POS;
        int addToYPos = PADDING_TOP;
        if (station.number > 0) {
            String stationNumber = String.valueOf(station.number);
            g.drawString(stationNumber, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);
            addToXPos += FontUtil.SMALL_BOLD.stringWidth(stationNumber);
            String stationSeparator = " - ";
            g.drawString(stationSeparator, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);
            addToXPos += FontUtil.SMALL_BOLD.stringWidth(stationSeparator);
        }
        
        String stationName = station.name != null ? station.name : "Station sans nom";
        g.drawString(stationName, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        addToXPos = BASE_LEFT_POS;
    	addToYPos += LINE_SPACING + FontUtil.SMALL.getHeight();
        if (station.fullAddress != null) {
	    	if (isSelected) {
	    		Color listFontSecondSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
	    		g.setColor(listFontSecondSelectedColor.intValue());
	    	}
	    	else {
	    		Color listFontSecondColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
	    		g.setColor(listFontSecondColor.intValue());
	    	}
	        g.setFont(FontUtil.SMALL);
	    	g.drawString(station.fullAddress, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        	addToYPos += LINE_SPACING + FontUtil.SMALL.getHeight();
        }
        
        if(station.zipCode != null || station.city != null) {
	    	if (isSelected) {
	    		Color listFontSecondSelectedColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
	    		g.setColor(listFontSecondSelectedColor.intValue());
	    	}
	    	else {
	    		Color listFontSecondColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
	    		g.setColor(listFontSecondColor.intValue());
	    	}
	        g.setFont(FontUtil.SMALL);
	        String secondaryStationInfo = (station.zipCode != null && station.zipCode.length() > 0 ? (station.zipCode + " - ") : "") + (station.city != null && station.city.length() > 0 ? station.city : "");
	    	g.drawString(secondaryStationInfo, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);
        }
	}

	public int computeHeight(AbstractListView view, Object itemObject, int offset) {
    	
//    	boolean isSelected = view.isItemSelected(offset);

    	int smallFontHeight = FontUtil.SMALL.getHeight();
        int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
        
        int itemHeight = 
        	PADDING_TOP + 
        	smallBoldFontHeight + 
        	LINE_SPACING + 
        	smallFontHeight + 
        	LINE_SPACING + 
        	smallFontHeight + 
        	PADDING_TOP;
 	
        return itemHeight;
	}

}
