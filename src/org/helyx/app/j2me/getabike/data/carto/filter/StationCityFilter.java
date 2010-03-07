package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.filter.Filter;

public class StationCityFilter implements Filter {

	private String city;

	public StationCityFilter(String zipCode) {
		super();
		this.city = zipCode;
	}

	public boolean matches(Object object) {
		Station station = (Station)object;
		
		if (station == null) {
			return false;
		}
		
		if (city == null) {
			return true;
		}
		
		if (station.city == null) {
			return false;
		}
		
		return station.city.toUpperCase().indexOf(city) > -1;
	}

	public String getCity() {
		return city;
	}
	
	public void setCity(String stationName) {
		this.city = stationName;
	}

}
