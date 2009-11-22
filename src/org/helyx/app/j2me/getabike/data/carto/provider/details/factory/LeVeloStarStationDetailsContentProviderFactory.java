package org.helyx.app.j2me.getabike.data.carto.provider.details.factory;

import java.util.Random;

import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.details.LeVeloStarStationDetailsContentProvider;
import org.helyx.app.j2me.getabike.data.carto.provider.details.VelibStationDetailsContentProvider;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.helyx4me.text.StringFormat;
import org.helyx.logging4me.Logger;


public class LeVeloStarStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("VELO_PLUS_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public LeVeloStarStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		String url = new StringFormat(city.stationDetails).format(new String[] { 
				String.valueOf(station.number)
			});
		if (logger.isDebugEnabled()) {
			logger.debug("Station details URL: " + url);
		}
		IContentProvider stationContentProvider = new LeVeloStarStationDetailsContentProvider(
			new HttpGetABikeContentAccessor(url), city, station
		);
		
		return stationContentProvider;
	}

}
