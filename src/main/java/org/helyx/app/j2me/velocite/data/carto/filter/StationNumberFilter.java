package org.helyx.app.j2me.velocite.data.carto.filter;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

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
