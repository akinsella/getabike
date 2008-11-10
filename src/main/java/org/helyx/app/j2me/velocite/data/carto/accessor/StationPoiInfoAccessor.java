package org.helyx.app.j2me.velocite.data.carto.accessor;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.map.google.POIInfoAccessor;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class StationPoiInfoAccessor implements POIInfoAccessor {

	private static final Log log = LogFactory.getLog("STATION_POI_INFO_ACCESSOR");
	
	public StationPoiInfoAccessor() {
		super();
	}

	public String getName(Object object) {
		if (object == null) {
			return "";
		}
		Station station = ((Station)object);

		return station.name;
	}

	public String getDescription(Object object) {
		if (object == null) {
			return "";
		}
		Station station = ((Station)object);

		return station.fullAddress;
	}

	public Point getLocalization(Object object) {
		if (object == null) {
			return null;
		}
		Station station = ((Station)object);
		
		log.info("station: " + station);
		return station.hasLocalization ? station.localization : null;
	}

}
