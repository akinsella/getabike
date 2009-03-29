package org.helyx.app.j2me.velocite.data.carto.filter;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.helyx4me.filter.IObjectFilter;
import org.helyx.helyx4me.math.DistanceUtil;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;

public class StationDistanceFilter implements IObjectFilter {
	
	private Logger logger = LoggerFactory.getLogger("STATION_DISTANCE_FILTER");
	
	private Station station;
	
	private int distanceMax;
	
	public StationDistanceFilter(Station station, int distanceMax) {
		super();
		this.station = station;
		this.distanceMax = distanceMax;
	}
	
	public boolean matches(Object object) {
		Station targetStation = (Station)object;
		double distanceInMeters = DistanceUtil.distance(
				station.localization.lat,
				station.localization.lng,
				targetStation.localization.lat,
				targetStation.localization.lng, 
				DistanceUtil.KM) * 1000;
		
		boolean matches = distanceInMeters < distanceMax;
		if (matches) {
			logger.info("Distance is: '" + distanceInMeters + "' meters between " + targetStation.name + "[" + targetStation.number + "] and " + station.name + "[" + station.number + "]");
		}
		return matches;
	}

	public int getDistanceMax() {
		return distanceMax;
	}

	public void setDistanceMax(int distanceMax) {
		this.distanceMax = distanceMax;
	}

}
