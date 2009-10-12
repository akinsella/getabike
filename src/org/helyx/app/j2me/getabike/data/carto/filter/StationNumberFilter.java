package org.helyx.app.j2me.getabike.data.carto.filter;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.helyx4me.filter.IRecordFilter;
import org.helyx.logging4me.Logger;


public class StationNumberFilter implements IRecordFilter {

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
