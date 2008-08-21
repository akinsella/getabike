package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.OrleansStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class OrleansStationContentProviderFactory implements ICityContentProviderFactory {

	private static final Log log = LogFactory.getLog("ORLEAN_STATION_CONTENT_PROVIDER_FACTORY");
	
	public OrleansStationContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(City city) {
		IContentAccessor stationContentAccessor = new HttpContentAccessor(city.stationList);

		IContentProvider stationContentProvider = new OrleansStationContentProvider(stationContentAccessor);  
		
		return stationContentProvider;
	}

}
