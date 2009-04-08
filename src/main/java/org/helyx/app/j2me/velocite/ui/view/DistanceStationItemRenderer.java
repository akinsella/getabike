package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.helyx4me.math.DistanceUtil;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;

public class DistanceStationItemRenderer extends StationItemRenderer {

	private Logger logger = LoggerFactory.getLogger("DISTANCE_STATION_ITEM_RENDERER");
	
	public DistanceStationItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		super.paintItem(view, g, offset, itemClientArea, itemObject);
		StationListView stationListView = (StationListView)view;
		Station referentStation = stationListView.getReferentStation();
		Station station = (Station)itemObject;
    	
		double distance = DistanceUtil.distance(referentStation.localization, station.localization, DistanceUtil.KM);
		logger.info("Distance: " + new String((long)(distance * 1000) + " m"));
		logger.info("itemClientArea: " + itemClientArea);
		if (distance < Double.MAX_VALUE) {
	        g.setFont(FontUtil.SMALL);

	        int x = itemClientArea.location.x;
	        int y = itemClientArea.location.y;
	        int width = itemClientArea.size.width;
	        int height = itemClientArea.size.height;
			String distanceStr = new String((long)(distance * 1000) + " m");
			g.drawString(distanceStr, x + width - FontUtil.SMALL.stringWidth(distanceStr) - 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.LEFT | Graphics.TOP);

			logger.info("y + height - 2: " + (y + height - 2));
		}
	}

}
