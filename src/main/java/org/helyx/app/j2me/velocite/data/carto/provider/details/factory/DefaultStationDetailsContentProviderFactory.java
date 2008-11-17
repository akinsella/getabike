package org.helyx.app.j2me.velocite.data.carto.provider.details.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.content.provider.exception.ContentProviderFactoryException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.text.StringFormat;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.details.DefaultStationDetailsContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class DefaultStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Log log = LogFactory.getLog("DEFAULT_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public DefaultStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		String url = new StringFormat(city.stationDetails).format(String.valueOf(station.number));
		log.debug("URL: " + url);
		IContentProvider stationContentProvider = new DefaultStationDetailsContentProvider(
				new HttpContentAccessor(url),
				city, 
				station );
		
		return stationContentProvider;
	}

}
