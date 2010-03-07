package org.helyx.app.j2me.getabike.data.carto.provider.factory;

import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.getabike.data.carto.provider.AbstractStationContentProvider;
import org.helyx.app.j2me.getabike.data.carto.provider.CityBikeStationContentProvider;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderFactoryException;
import org.helyx.logging4me.Logger;


public class CityBikeStationContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = Logger.getLogger("CITY_BIKE_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;

	public CityBikeStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		try {
			AbstractStationContentProvider stationContentProvider = new CityBikeStationContentProvider(
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
