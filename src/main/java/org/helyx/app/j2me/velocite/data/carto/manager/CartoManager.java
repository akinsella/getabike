package org.helyx.app.j2me.velocite.data.carto.manager;

import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.velocite.data.carto.listener.StationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.HttpStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.ICityContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.LyonStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.OrleansStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.StationContentProviderFactoryNoFoundExcepton;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.listener.CityLoaderProgressListener;

public class CartoManager {

	private static final String CAT = "CARTO_MANAGER";
	
	private static final String DEFAULT = "DEFAULT";
	private static final String LYON = "LYON";
	private static final String ORLEANS = "ORLEANS";
	
	private CartoManager() {
		super();
	}
	
	public static IProgressTask refreshAll(City city) throws CartoManagerException {

		try {
			ICityContentProviderFactory cpf = null;
			if (DEFAULT.equals(city.contentProviderFactory)) {
				cpf = new HttpStationContentProviderFactory();
			}
			else if (LYON.equals(city.contentProviderFactory)) {
				cpf = new LyonStationContentProviderFactory();
			}
			else if (ORLEANS.equals(city.contentProviderFactory)) {
				cpf = new OrleansStationContentProviderFactory();
			}
			else {
				throw new StationContentProviderFactoryNoFoundExcepton("ContentProviderFactory searched for city key: '" + city.key + "'");
			}
			
			IContentProvider cp = cpf.getContentProviderFactory(city);
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
	
			return progressTask;
		}
		catch (StationContentProviderFactoryNoFoundExcepton e) {
			throw new CartoManagerException(e);
		}

	}

	public static void cleanUpSavedData() {
		StationPersistenceService stationPersistenceService = new StationPersistenceService();
		try {
			stationPersistenceService.removeAllStations();
		}
		finally {
			stationPersistenceService.dispose();
		}	
	}
	
}
