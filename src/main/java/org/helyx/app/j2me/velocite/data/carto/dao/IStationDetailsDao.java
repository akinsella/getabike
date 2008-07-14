package org.helyx.app.j2me.velocite.data.carto.dao;

import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;

public interface IStationDetailsDao {

	int saveStationDetails(StationDetails stationDetails);

	StationDetails findStationDetailsByNumber(int stationNumber);
	
	void dispose();

}
