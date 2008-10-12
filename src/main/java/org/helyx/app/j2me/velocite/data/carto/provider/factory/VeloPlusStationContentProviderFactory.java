package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.VeloPlusStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class VeloPlusStationContentProviderFactory implements IContentProviderFactory {

	private static final Log log = LogFactory.getLog("VELO_PLUS_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	
	public VeloPlusStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider getContentProviderFactory() {
		IContentAccessor stationContentAccessor = new HttpContentAccessor(city.stationList);

		IContentProvider stationContentProvider = new VeloPlusStationContentProvider(stationContentAccessor);  
		
		return stationContentProvider;
	}

}
