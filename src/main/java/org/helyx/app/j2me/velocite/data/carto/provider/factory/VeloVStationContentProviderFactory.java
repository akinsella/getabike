package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessorFactory;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.content.provider.exception.ContentProviderException;
import org.helyx.app.j2me.lib.content.provider.exception.ContentProviderFactoryException;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.text.StringFormat;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.carto.provider.AbstractStationContentProvider;
import org.helyx.app.j2me.velocite.data.carto.provider.VeloVStationContentProvider;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.domain.Quartier;

public class VeloVStationContentProviderFactory implements IContentProviderFactory {

	private static final Logger logger = LoggerFactory.getLogger("VELOV_STATION_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	
	public VeloVStationContentProviderFactory(City city) {
		super();
		this.city = city;
	}
	
	public IContentProvider createContentProvider() throws ContentProviderFactoryException {
		try {
			AbstractStationContentProvider stationContentProvider = new VeloVStationContentProvider(city, new IContentAccessorFactory() {

				public IContentAccessor createContentAccessorFactory(Object object) {
					
					Quartier quartier = (Quartier)object;
					String url = new StringFormat(city.stationList).format(quartier.zipCode);
					logger.info("Url: " + url);
					return new HttpContentAccessor(url);
				}
				
			});
			
			IStationInfoNormalizer stationInfoNormalizer = CartoManager.getStationInfoNormalizer(city);
			stationContentProvider.setStationInfoNormalizer(stationInfoNormalizer);
			
			return stationContentProvider;
		}
		catch(CartoManagerException cme) {
			throw new ContentProviderFactoryException(cme);
		}
	}

}
