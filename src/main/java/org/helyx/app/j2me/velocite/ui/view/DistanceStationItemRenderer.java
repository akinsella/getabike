package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.DistanceUtil;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class DistanceStationItemRenderer extends StationItemRenderer {

	private Log log = LogFactory.getLog("DISTANCE_STATION_ITEM_RENDERER");
	
	public DistanceStationItemRenderer() {
		super();
	}

	public void paintItem(AbstractListView view, Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		super.paintItem(view, g, offset, itemClientArea, itemObject);
		StationListView stationListView = (StationListView)view;
		Station referentStation = stationListView.getReferentStation();
		Station station = (Station)itemObject;
    	
		double distance = DistanceUtil.distance(referentStation.localization, station.localization, DistanceUtil.KM);
		log.info("Distance: " + new String((long)(distance * 1000) + " m"));
		log.info("itemClientArea: " + itemClientArea);
		if (distance < Double.MAX_VALUE) {
	        g.setFont(FontUtil.SMALL);

	        int x = itemClientArea.location.x;
	        int y = itemClientArea.location.y;
	        int width = itemClientArea.size.width;
	        int height = itemClientArea.size.height;
			String distanceStr = new String((long)(distance * 1000) + " m");
			g.drawString(distanceStr, x + width - FontUtil.SMALL.stringWidth(distanceStr) - 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.LEFT | Graphics.TOP);

			log.info("y + height - 2: " + (y + height - 2));
		}
	}

}
