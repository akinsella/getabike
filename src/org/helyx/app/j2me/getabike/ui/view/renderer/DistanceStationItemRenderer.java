package org.helyx.app.j2me.getabike.ui.view.renderer;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.math.DistanceUtil;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.logging4me.Logger;

public class DistanceStationItemRenderer extends StationItemRenderer {

	private Logger logger = Logger.getLogger("DISTANCE_STATION_ITEM_RENDERER");
	
	private static String distanceInMeter = " m";
	private static String distanceInKM = " km";
	private boolean useLocation;

	
	public DistanceStationItemRenderer() {
		this(false);
	}
	
	public DistanceStationItemRenderer(boolean useLocation) {
		super();
		this.useLocation = useLocation;
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
 
		super.paintItem(view, g, offset, itemClientArea, itemObject);

		boolean isSelected = view.isItemSelected(offset);

		StationListView stationListView = (StationListView)view;
		
		Point location = null;
		if (useLocation) {
			location = stationListView.getLocation();
		}
		else {
			location = stationListView.getReferentStation().localization;
		}
		
		Station station = (Station)itemObject;
		
		if (location != null) {
			station.distance = DistanceUtil.distance(location, station.localization, DistanceUtil.KM) * 1000;
		}
    	
		if (logger.isDebugEnabled()) {
			logger.debug("Distance: " + new String((long)(station.distance) + distanceInMeter));
			logger.debug("itemClientArea: " + itemClientArea);
		}
		
		if (station.distance < Double.MAX_VALUE) {

	        int x = itemClientArea.location.x;
	        int y = itemClientArea.location.y;
	        int width = itemClientArea.size.width;
	        int height = itemClientArea.size.height;
	        
			String distanceStr = null;
			
			if (station.distance < 10000) {
				distanceStr = new String((long)(station.distance) + distanceInMeter);
			}
			else {
				distanceStr = new String((long)(station.distance / 1000) + distanceInKM);
			}
			
			Color distanceFontColor = null;
			
			if (isSelected) {
				distanceFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_THIRD_SELECTED);
			}
			else {
				distanceFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_THIRD);
			}

			g.setFont(FontUtil.SMALL);
			g.setColor(distanceFontColor.intValue());

			g.drawString(distanceStr, x + width - FontUtil.SMALL.stringWidth(distanceStr) - 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.LEFT | Graphics.TOP);

			if (logger.isDebugEnabled()) {
				logger.debug("y + height - 2: " + (y + height - 2));
			}
		}
	}

}
