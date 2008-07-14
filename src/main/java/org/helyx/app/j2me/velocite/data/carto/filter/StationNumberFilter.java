package org.helyx.app.j2me.velocite.data.carto.filter;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;

public class StationNumberFilter implements IRecordFilter {

	private static final String CAT = "STATION_NUMBER_FILTER";
	
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
