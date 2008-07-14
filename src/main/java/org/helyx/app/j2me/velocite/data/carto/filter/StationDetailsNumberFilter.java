package org.helyx.app.j2me.velocite.data.carto.filter;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.rms.RecordFilter;

public class StationDetailsNumberFilter implements RecordFilter {

	private static final String CAT = "STATION_DETAILS_NUMBER_FILTER";
	
	private int stationNumberFilter;
	
	public StationDetailsNumberFilter(int stationNumberFilter) {
		super();
		this.stationNumberFilter = stationNumberFilter;
	}

	public boolean matches(byte[] bytes) {
		try {
			DataInputStream dis = null;
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	
				dis = new DataInputStream(bis);
				int stationNumber = dis.readInt();
				return this.stationNumberFilter == stationNumber;
			}
			finally {
				if (dis != null) {
					dis.close();
				}
			}
		}
		catch(IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}


}
