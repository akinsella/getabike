package org.helyx.app.j2me.getabike.data.carto.filter.record;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.helyx4me.filter.record.RecordFilter;
import org.helyx.logging4me.Logger;


public class StationNumberFilter implements RecordFilter {

	private static final Logger logger = Logger.getLogger("STATION_NUMBER_FILTER");
	
	private int stationNumberFilter;
	
	public StationNumberFilter(int stationNumberFilter) {
		super();
		this.stationNumberFilter = stationNumberFilter;
	}

	public boolean matches(DataInputStream dis) throws IOException {
		dis.reset();

		int stationNumber = dis.readInt();
		
		return stationNumberFilter == stationNumber;
	}
	
}
