package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessorFactory;
import org.helyx.app.j2me.lib.content.accessor.SingleParamHttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.VeloVStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.domain.Quartier;

public class VeloVStationContentProviderFactory implements ICityContentProviderFactory {

	private static final Log log = LogFactory.getLog("VELOV_STATION_CONTENT_PROVIDER_FACTORY");
	
	public VeloVStationContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(final City city) {
		IContentProvider stationContentProvider = new VeloVStationContentProvider(city, new IContentAccessorFactory() {

			public IContentAccessor createContentAccessorFactory(Object object) {
				Quartier quartier = (Quartier)object;
				return new SingleParamHttpContentAccessor(city.stationList, quartier.zipCode);
			}
			
		});    			
		
		return stationContentProvider;
	}

}
