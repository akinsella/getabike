package org.helyx.app.j2me.velocite.data.carto.filter;

import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class StationNameFilter implements IRecordFilter {

	private static final Logger logger = LoggerFactory.getLogger("STATION_NAME_FILTER_3");
	
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
