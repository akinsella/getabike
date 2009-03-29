package org.helyx.app.j2me.velocite.data.carto.filter;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.helyx4me.filter.IRecordFilter;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;

public class StationNumberFilter implements IRecordFilter {

	private static final Logger logger = LoggerFactory.getLogger("STATION_NUMBER_FILTER");
	
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
