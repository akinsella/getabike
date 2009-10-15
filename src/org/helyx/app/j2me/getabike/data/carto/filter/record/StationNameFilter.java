package org.helyx.app.j2me.getabike.data.carto.filter.record;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.helyx4me.filter.record.RecordFilter;
import org.helyx.logging4me.Logger;


public class StationNameFilter implements RecordFilter {

	private static final Logger logger = Logger.getLogger("STATION_NAME_FILTER_3");
	
	private String stationNameFilter;
	
	public StationNameFilter(String stationNameFilter) {
		super();
		this.stationNameFilter = stationNameFilter.toUpperCase();
	}

	public boolean matches(DataInputStream dis) throws IOException {
		dis.reset();

		dis.readInt();
		String stationName = dis.readUTF();
		
		return stationName.toUpperCase().indexOf(stationNameFilter) > -1;
	}

}
