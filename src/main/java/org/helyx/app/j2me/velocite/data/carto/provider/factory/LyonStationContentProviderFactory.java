package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.SingleParamHttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.LyonStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class LyonStationContentProviderFactory implements ICityContentProviderFactory {

	private static final Log log = LogFactory.getLog("LYON_STATION_CONTENT_PROVIDER_FACTORY");
	
	public LyonStationContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(City city) {
		int length = 11;
		IContentAccessor[] stationContentAccessors = new IContentAccessor[11];
		for (int i = 0  ; i < length ; i++) {
			stationContentAccessors[i] = new SingleParamHttpContentAccessor(city.stationList, String.valueOf(i + 1));
		}
		IContentProvider stationContentProvider = new LyonStationContentProvider(stationContentAccessors);    			
		
		return stationContentProvider;
	}

}
