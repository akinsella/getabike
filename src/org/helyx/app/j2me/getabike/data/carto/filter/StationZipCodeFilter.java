package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.filter.Filter;

public class StationZipCodeFilter implements Filter {

	private String zipCode;

	public StationZipCodeFilter(String zipCode) {
		super();
		this.zipCode = zipCode;
	}

	public boolean matches(Object object) {
		Station station = (Station)object;
		
		if (station == null) {
			return false;
		}
		
		if (zipCode == null) {
			return true;
		}
		
		if (station.zipCode == null) {
			return false;
		}
		
		return station.zipCode.toUpperCase().indexOf(zipCode) > -1;
	}

	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String stationName) {
		this.zipCode = stationName;
	}

}
