package org.helyx.app.j2me.velocite.data.carto.comparator;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.rms.RecordComparator;

public class StationDetailsDateCreationComparator implements RecordComparator {

	private static final String CAT = "STATION_NAME_COMPARATOR";
	
	public int compare(byte[] bytes1, byte[] bytes2) {
		long stationDate1 = readStationDetailsDateCreation(bytes1);
		long stationDate2 = readStationDetailsDateCreation(bytes2);
		
		long result = stationDate1 - stationDate2;
		if (result == 0){
			return RecordComparator.EQUIVALENT;
		}
		else if (result > 0)	{
			return RecordComparator.FOLLOWS;
		}
		else {
			return RecordComparator.PRECEDES;
		}
	}
	
	private long readStationDetailsDateCreation(byte[] bytes) {
		try {
			DataInputStream dis = null;
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	
				dis = new DataInputStream(bis);
				dis.readInt();
				long dateCreation = dis.readLong();
	
				return dateCreation;
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
