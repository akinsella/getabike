package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.DefaultStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class DefaultStationContentProviderFactory implements IContentProviderFactory {

	private static final String CAT = "DEFAULT_STATION_CONTENT_PROVIDER_FACTORY";
	
	public DefaultStationContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(City city) {
		IContentProvider stationContentProvider = new DefaultStationContentProvider(new HttpContentAccessor(city.stationList));
		
		return stationContentProvider;
	}

}
