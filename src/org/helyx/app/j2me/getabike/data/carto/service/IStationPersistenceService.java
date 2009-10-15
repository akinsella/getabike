package org.helyx.app.j2me.getabike.data.carto.service;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.helyx4me.filter.record.RecordFilter;
import org.helyx.helyx4me.rms.MultiRecordEnumeration;

public interface IStationPersistenceService {
	
	Station findStationByNumber(int stationNumber) throws StationNotFoundException;
	
	void saveStationArray(Station[] stationArray);
	
	Vector findAllStations();

	void removeAllStations();

	MultiRecordEnumeration createStationEnumeration(RecordFilter recordFilter);
	
	void destroyStationEnumeration(MultiRecordEnumeration stationEnumeration);

	int countStations();
	
	void dispose();

}


