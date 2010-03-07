package org.helyx.app.j2me.getabike.ui.view.renderer;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.ICellRenderer;
import org.helyx.logging4me.Logger;


public class StationItemRenderer implements ICellRenderer {

	private Logger logger = Logger.getLogger("STATION_ITEM_RENDERER");
	
	private static final int BASE_LEFT_POS = 5;
	private static final int PADDING_TOP = 2;
	private static final int LINE_SPACING = 0;

	private int smallFontHeight = FontUtil.SMALL.getHeight();
	private int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
    private static String stationSeparator = " - ";
    private static String stationSeparatorEmpty = "";
    private int stationSeparatorStrWidthSmallBold = FontUtil.SMALL_BOLD.stringWidth(stationSeparator);
	
	public StationItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		Station station = (Station)itemObject;
    	
    	boolean isSelected = view.isItemSelected(offset);
		int listFontSecondSelectedColor = view.getTheme().getColor(isSelected ? ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED : ThemeConstants.WIDGET_LIST_FONT_SECOND).intValue();
   	
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
             g.drawString(stationSeparator, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);
            addToXPos += stationSeparatorStrWidthSmallBold;
        }
        
        String stationName = station.name != null ? station.name : view.getMessage("renderer.station.no.name");
        g.drawString(stationName, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        addToXPos = BASE_LEFT_POS;
    	addToYPos += LINE_SPACING + smallFontHeight;
        if (station.fullAddress.length() > 0) {

    		g.setColor(listFontSecondSelectedColor);
	        g.setFont(FontUtil.SMALL);
	    	g.drawString(station.fullAddress, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);

        	addToYPos += LINE_SPACING + smallFontHeight;
        }
        
        if(station.zipCode.length() > 0 || station.city.length() > 0) {
	        String secondaryStationInfo = (station.zipCode.length() > 0 ? (station.zipCode + stationSeparator) : stationSeparatorEmpty) + (station.city.length() > 0 ? station.city : "");

    		g.setColor(listFontSecondSelectedColor);
	        g.setFont(FontUtil.SMALL);
	    	g.drawString(secondaryStationInfo, x + addToXPos, y + addToYPos, Graphics.LEFT | Graphics.TOP);
        }
	}

	public int computeHeight(AbstractListView view, Object itemObject, int offset) {

		Station station = (Station)itemObject;
    	
//    	boolean isSelected = view.isItemSelected(offset);
        
        int itemHeight = 
        	PADDING_TOP + 
        	smallBoldFontHeight + LINE_SPACING + 
        	(station.fullAddress.length() > 0 ? smallFontHeight : 0) + 
        	(station.zipCode.length() > 0 || station.city.length() > 0 ? LINE_SPACING + smallFontHeight : 0) + 
        	PADDING_TOP;
 	
        return itemHeight;
	}

}
