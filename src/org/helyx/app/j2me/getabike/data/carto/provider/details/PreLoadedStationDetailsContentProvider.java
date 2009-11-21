package org.helyx.app.j2me.getabike.data.carto.provider.details;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.content.provider.AbstractContentProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.logging4me.Logger;

public class PreLoadedStationDetailsContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("CITY_BIKE_STATION_DETAILS_CONTENT_PROVIDER");

	private City city;
	private Station station;
	
	public PreLoadedStationDetailsContentProvider(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public void loadData() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Loading station '" + station.number + "' infos ...");
		}
	
		try {			
			progressDispatcher.fireEvent(EventType.ON_START);

			StationDetails stationDetails = station.details;
			
			if (logger.isInfoEnabled()) {
				logger.info("Station details: '" + stationDetails + "'");
			}

			progressDispatcher.fireEvent(EventType.ON_SUCCESS, stationDetails);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}

	}
	
	public String getDescription() {
		return "Station: '" + station + "'";
	}

}
