package org.helyx.app.j2me.velocite.data.carto.service;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public interface IStationPersistenceService {
	
	Station findStationByNumber(int stationNumber) throws StationNotFoundException;
	
	void saveStationArray(Station[] stationArray);
	
	Vector findAllStations();

	void removeAllStations();

	MultiRecordEnumeration createStationEnumeration(IRecordFilter recordFilter);
	
	void destroyStationEnumeration(MultiRecordEnumeration stationEnumeration);

	int countStations();
	
	void dispose();

}


