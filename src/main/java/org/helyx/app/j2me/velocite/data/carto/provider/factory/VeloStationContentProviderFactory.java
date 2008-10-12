package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.VeloStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class VeloStationContentProviderFactory implements IContentProviderFactory {

	private static final Log log = LogFactory.getLog("VELO_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	
	public VeloStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider getContentProviderFactory() {
		IContentProvider stationContentProvider = new VeloStationContentProvider(new HttpContentAccessor(city.stationList));
		
		return stationContentProvider;
	}

}
