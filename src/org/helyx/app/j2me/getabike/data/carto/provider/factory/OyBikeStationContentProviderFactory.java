package org.helyx.app.j2me.getabike.data.carto.provider.factory;

import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.getabike.data.carto.provider.AbstractStationContentProvider;
import org.helyx.app.j2me.getabike.data.carto.provider.OyBikeStationContentProvider;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.logging4me.Logger;


public class OyBikeStationContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("OYBIKE_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;

	public OyBikeStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		try {
			AbstractStationContentProvider stationContentProvider = new OyBikeStationContentProvider(
					city,
					new HttpGetABikeContentAccessor(city.stationList));
			IStationInfoNormalizer stationInfoNormalizer = CartoManager.getStationInfoNormalizer(city);
			stationContentProvider.setStationInfoNormalizer(stationInfoNormalizer);
			return stationContentProvider;
		}
		catch(CartoManagerException cme) {
			throw new ContentProviderFactoryException(cme);
		}
	}

}
