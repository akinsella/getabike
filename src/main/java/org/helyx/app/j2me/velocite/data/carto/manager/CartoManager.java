package org.helyx.app.j2me.velocite.data.carto.manager;

import org.helyx.app.j2me.lib.content.provider.ContentProviderFactoryNotFoundExcepton;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.details.factory.DefaultStationDetailsContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.DefaultStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloPlusStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloVStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class CartoManager {

	private static final Log log = LogFactory.getLog("CARTO_MANAGER");
	
	private static final String VELIB = "VELIB";  // PARIS
	private static final String VELO_PLUS = "VELO_PLUS";  // ORLEANS
	private static final String VELO = "VELO";  // TOULOUSE
	private static final String VELO_V = "VELO_V";  // LYON
	private static final String LE_VELO = "LE_VELO";  // MARSEILLE
	private static final String SEVICI = "SEVICI";  // SEVILLE
	
	private CartoManager() {
		super();
	}
	
	public static IProgressTask refreshAll(City city) throws CartoManagerException {

		try {
			IContentProviderFactory cpf = null;
			if (VELIB.equals(city.type)) {
				cpf = new DefaultStationContentProviderFactory(city);
			}
			else if (LE_VELO.equals(city.type)) {
				cpf = new VeloStationContentProviderFactory(city);
			}
			else if (VELO.equals(city.type)) {
				cpf = new VeloStationContentProviderFactory(city);
			}
			else if (SEVICI.equals(city.type)) {
				cpf = new DefaultStationContentProviderFactory(city);
			}
			else if (VELO_V.equals(city.type)) {
				cpf = new VeloVStationContentProviderFactory(city);
			}
			else if (VELO_PLUS.equals(city.type)) {
				cpf = new VeloPlusStationContentProviderFactory(city);
			}
			else {
				throw new ContentProviderFactoryNotFoundExcepton("No ContentProviderFactory for city type: '" + city.type + "' and key: '" + city.key + "'");
			}
			
			IContentProvider cp = cpf.getContentProviderFactory();
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
	
			return progressTask;
		}
		catch (ContentProviderFactoryNotFoundExcepton e) {
			throw new CartoManagerException(e);
		}

	}

	public static IProgressTask fetchStationDetails(City city, Station station) throws CartoManagerException {

		try {
			IContentProviderFactory cpf = null;
			if (VELIB.equals(city.type)) {
				cpf = new DefaultStationDetailsContentProviderFactory(city, station);
			}
			else if (LE_VELO.equals(city.type)) {
				cpf = new DefaultStationDetailsContentProviderFactory(city, station);
			}
			else if (SEVICI.equals(city.type)) {
				cpf = new DefaultStationDetailsContentProviderFactory(city, station);
			}
			else if (VELO.equals(city.type)) {
				cpf = new DefaultStationDetailsContentProviderFactory(city, station);
			}
			else {
				throw new ContentProviderFactoryNotFoundExcepton("No ContentProviderFactory for city type: '" + city.type + "' and key: '" + city.key + "'");
			}
			
			IContentProvider cp = cpf.getContentProviderFactory();
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
	
			return progressTask;
		}
		catch (ContentProviderFactoryNotFoundExcepton e) {
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
