package org.helyx.app.j2me.getabike.data.carto.provider.details.factory;

import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.details.VelibStationDetailsContentProvider;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderFactoryException;
import org.helyx.app.j2me.getabike.lib.text.StringFormat;
import org.helyx.logging4me.Logger;


public class VelibStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("VELIB_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public VelibStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		String url = new StringFormat(city.stationDetails).format(String.valueOf(station.number));
		if (logger.isDebugEnabled()) {
			logger.debug("Station details URL: " + url);
		}
		IContentProvider stationContentProvider = new VelibStationDetailsContentProvider(
				new HttpGetABikeContentAccessor(url),
				city, 
				station );
		
		return stationContentProvider;
	}

}
