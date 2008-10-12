package org.helyx.app.j2me.velocite.data.carto.provider.details.factory;

import org.helyx.app.j2me.lib.content.accessor.SingleParamHttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
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
	
	public IContentProvider getContentProviderFactory() {
		IContentProvider stationContentProvider = new DefaultStationDetailsContentProvider(new SingleParamHttpContentAccessor(city.stationDetails, String.valueOf(station.number)));
		
		return stationContentProvider;
	}

}
