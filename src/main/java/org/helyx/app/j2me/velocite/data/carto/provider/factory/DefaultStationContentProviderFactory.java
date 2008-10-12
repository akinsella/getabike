package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.DefaultStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class DefaultStationContentProviderFactory implements IContentProviderFactory {

	private static final Log log = LogFactory.getLog("DEFAULT_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;

	public DefaultStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider getContentProviderFactory() {
		IContentProvider stationContentProvider = new DefaultStationContentProvider(new HttpContentAccessor(city.stationList));
		
		return stationContentProvider;
	}

}
