package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.helyx4me.filter.Filter;

public class StationNameFilter implements Filter {

	private String stationName;

	public StationNameFilter(String stationName) {
		super();
		this.stationName = stationName;
	}

	public boolean matches(Object object) {
		Station station = (Station)object;
		
		if (station == null) {
			return false;
		}
		
		if (stationName == null) {
			return true;
		}
		
		if (station.name == null) {
			return false;
		}
		
		return station.name.toUpperCase().indexOf(stationName) > -1;
	}
	
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

}
