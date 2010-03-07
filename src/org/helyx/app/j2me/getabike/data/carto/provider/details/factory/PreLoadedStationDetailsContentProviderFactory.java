package org.helyx.app.j2me.getabike.data.carto.provider.details.factory;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.details.PreLoadedStationDetailsContentProvider;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderFactoryException;
import org.helyx.logging4me.Logger;

public class PreLoadedStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("VELIB_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public PreLoadedStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("City: " + city + ", station: " + station);
		}
		IContentProvider stationContentProvider = new PreLoadedStationDetailsContentProvider(city, station);
		
		return stationContentProvider;
	}
}
