package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.carto.provider.AbstractStationContentProvider;
import org.helyx.app.j2me.velocite.data.carto.provider.VeloStationContentProvider;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.logging4me.Logger;


public class VeloStationContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("VELO_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	
	public VeloStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		try {
			AbstractStationContentProvider stationContentProvider = new VeloStationContentProvider(new HttpContentAccessor(city.stationList));
			IStationInfoNormalizer stationInfoNormalizer = CartoManager.getStationInfoNormalizer(city);
			stationContentProvider.setStationInfoNormalizer(stationInfoNormalizer);
			return stationContentProvider;
		}
		catch(CartoManagerException cme) {
			throw new ContentProviderFactoryException(cme);
		}
	}

}
