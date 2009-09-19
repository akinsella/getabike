package org.helyx.app.j2me.velocite.ui.view.renderer;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;
import org.helyx.app.j2me.velocite.ui.view.StationListView;
import org.helyx.helyx4me.math.DistanceUtil;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.logging4me.Logger;

public class DistanceStationItemRenderer extends StationItemRenderer {

	private Logger logger = Logger.getLogger("DISTANCE_STATION_ITEM_RENDERER");
	
	public DistanceStationItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
 
		super.paintItem(view, g, offset, itemClientArea, itemObject);

		boolean isSelected = view.isItemSelected(offset);

		StationListView stationListView = (StationListView)view;
		Station referentStation = stationListView.getReferentStation();
		Station station = (Station)itemObject;
		
		if (station.distance == Double.MAX_VALUE) {
			station.distance = DistanceUtil.distance(referentStation.localization, station.localization, DistanceUtil.KM) * 1000;
		}
    	
		logger.info("Distance: " + new String((long)(station.distance) + " m"));
		logger.info("itemClientArea: " + itemClientArea);
		if (station.distance < Double.MAX_VALUE) {
	        g.setFont(FontUtil.SMALL);

	        int x = itemClientArea.location.x;
	        int y = itemClientArea.location.y;
	        int width = itemClientArea.size.width;
	        int height = itemClientArea.size.height;
			String distanceStr = new String((long)(station.distance) + " m");
			
			Color distanceFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
			if (isSelected) {
				distanceFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
			}
			try {
				//TODO: Add color to theme
				distanceFontColor = view.getTheme().getColor(AppThemeConstants.STATION_LIST_DISTANCE_COLOR);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
			g.setFont(FontUtil.SMALL);
			g.setColor(distanceFontColor.intValue());

			g.drawString(distanceStr, x + width - FontUtil.SMALL.stringWidth(distanceStr) - 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.LEFT | Graphics.TOP);

			logger.info("y + height - 2: " + (y + height - 2));
		}
	}

}
