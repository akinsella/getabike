package org.helyx.app.j2me.getabike.data.carto.service;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;

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


