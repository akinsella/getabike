package org.helyx.app.j2me.velocite.data.carto.provider.details.factory;

import java.util.Random;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.text.StringFormat;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.details.VeloVStationDetailsContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class VeloVStationDetailsContentProviderFactory implements IContentProviderFactory {

	private static final Log log = LogFactory.getLog("VELO_PLUS_STATION_DETAILS_CONTENT_PROVIDER_FACTORY");
	
	private City city;
	private Station station;
	
	public VeloVStationDetailsContentProviderFactory(City city, Station station) {
		super();
		this.city = city;
		this.station = station;
	}
	
	public IContentProvider getContentProviderFactory() {
		String url = new StringFormat(city.stationDetails).format(new String[] { 
				String.valueOf(station.number), 
				String.valueOf((long)(new Random().nextDouble() * 100000 + 1))
			});
		log.debug("URL: " + url);

		IContentProvider stationContentProvider = new VeloVStationDetailsContentProvider(
			new HttpContentAccessor(url), city, station
		);
		
		return stationContentProvider;
	}

}
