package org.helyx.app.j2me.velocite.data.carto.provider.details.factory;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.details.DefaultStationDetailsContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.helyx4me.text.StringFormat;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;

public class DefaultStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = LoggerFactory.getLogger("DEFAULT_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public DefaultStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		String url = new StringFormat(city.stationDetails).format(String.valueOf(station.number));
		logger.debug("URL: " + url);
		IContentProvider stationContentProvider = new DefaultStationDetailsContentProvider(
				new HttpContentAccessor(url),
				city, 
				station );
		
		return stationContentProvider;
	}

}
