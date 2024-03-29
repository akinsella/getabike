package org.helyx.app.j2me.getabike.data.carto.accessor;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.map.google.POIInfoAccessor;
import org.helyx.logging4me.Logger;


public class StationPoiInfoAccessor implements POIInfoAccessor {

	private static final Logger logger = Logger.getLogger("STATION_POI_INFO_ACCESSOR");
	
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
		
//		logger.info("station: " + station);
		return station.hasLocalization ? station.localization : null;
	}

}
