package org.helyx.app.j2me.velocite.data.carto.service;


import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.rms.IMultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.velocite.data.carto.dao.IStationDetailsDao;
import org.helyx.app.j2me.velocite.data.carto.dao.StationDetailsDao;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNumberFilter;
import org.helyx.app.j2me.velocite.data.carto.serializer.StationSerializer;

public class StationPersistenceService implements IStationPersistenceService {
	
	private static final String CAT = "STATION_PERSISTENCE_SERVICE";
	
	private static final String STATION_RECORD_STORE_NAME = "station";
	
	private IMultiRecordDao stationDao;
	private IStationDetailsDao stationDetailsDao;

	
	public StationPersistenceService() {
		super();
	}
	
	public void dispose() {
		if (stationDao != null) {
			stationDao.dispose();
		}
		if (stationDetailsDao != null) {
			stationDetailsDao.dispose();
		}
	}
	
	private IMultiRecordDao getStationDao() {
		if (stationDao == null) {
			stationDao = new MultiRecordDao(STATION_RECORD_STORE_NAME, new StationSerializer(), 1024);
		}
		
		return stationDao;
	}
	
	private IStationDetailsDao getStationDetailsDao() {
		if (stationDetailsDao == null) {
			stationDetailsDao = new StationDetailsDao();
		}
		
		return stationDetailsDao;
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

	public int saveStationDetails(StationDetails stationDetails) {
		int stationDetailsId = getStationDetailsDao().saveStationDetails(stationDetails);
		
		return stationDetailsId;
	}

	public MultiRecordEnumeration createStationEnumeration(IRecordFilter recordFilter) {
		return getStationDao().createRecordEnumeration(recordFilter);
	}

	public void destroyStationEnumeration(MultiRecordEnumeration stationEnumeration) {
		getStationDao().destroyRecordEnumeration(stationEnumeration);
	}

	public int countStations() {
		return getStationDao().countRecords();
	}

	public StationDetails findStationDetailsByNumber(int stationNumber) {
		return getStationDetailsDao().findStationDetailsByNumber(stationNumber);
	}

}


