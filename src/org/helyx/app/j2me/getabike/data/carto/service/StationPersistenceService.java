package org.helyx.app.j2me.getabike.data.carto.service;


import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.filter.record.StationNumberFilter;
import org.helyx.app.j2me.getabike.data.carto.serializer.StationSerializer;
import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.IMultiRecordDao;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordDao;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;
import org.helyx.logging4me.Logger;


public class StationPersistenceService implements IStationPersistenceService {
	
	private static final Logger logger = Logger.getLogger("STATION_PERSISTENCE_SERVICE");
	
	private static final String STATION_RECORD_STORE_NAME = "station";
	
	private IMultiRecordDao stationDao;

	public StationPersistenceService() {
		super();
	}
	
	public void dispose() {
		if (stationDao != null) {
			stationDao.dispose();
		}
	}
	
	private IMultiRecordDao getStationDao() {
		if (stationDao == null) {
			stationDao = new MultiRecordDao(STATION_RECORD_STORE_NAME, new StationSerializer(), 1024);
		}
		
		return stationDao;
	}

	public Vector findAllStations() {
		return getStationDao().findAllRecords();
	}

	public Station findStationByNumber(int stationNumber) throws StationNotFoundException {
		MultiRecordEnumeration multiRecordEnumeration = getStationDao().createRecordEnumeration(new StationNumberFilter(stationNumber));
		try {
			if (multiRecordEnumeration.hasMoreElements()) {
				Station station = (Station)multiRecordEnumeration.nextElement();
				return station;
			}
			throw new StationNotFoundException("Station: '" + stationNumber + "'");
		}
		finally {
			getStationDao().destroyRecordEnumeration(multiRecordEnumeration);
		}
	}

	public void removeAllStations() {
		getStationDao().removeAllRecords();
	}

	public void saveStationArray(Station[] stationArray) {
		getStationDao().saveRecordArray(stationArray);
	}

	public MultiRecordEnumeration createStationEnumeration(RecordFilter recordFilter) {
		return getStationDao().createRecordEnumeration(recordFilter);
	}

	public void destroyStationEnumeration(MultiRecordEnumeration stationEnumeration) {
		getStationDao().destroyRecordEnumeration(stationEnumeration);
	}

	public int countStations() {
		return getStationDao().countRecords();
	}

}


