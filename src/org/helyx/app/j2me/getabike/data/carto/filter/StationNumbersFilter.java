package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.filter.Filter;

public class StationNumbersFilter implements Filter {

	private int[] stationNumbers;
	private int stationNumberCount;
	
	public StationNumbersFilter(int[] stationNumbers) {
		super();
		this.stationNumbers = stationNumbers;
		this.stationNumberCount = stationNumbers.length;
	}

	public boolean matches(Object object) {
		Station station = (Station)object;
		
		if (station == null) {
			return false;
		}
		int stationNumber = station.number;
		
		for (int i = 0 ; i < stationNumberCount ; i++) {
			if (stationNumbers[i] == stationNumber) {
				return true;
			}
		}
		
		return false;
	}

}
