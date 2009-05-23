package org.helyx.app.j2me.velocite.data.carto.manager;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationDistanceFilter;
import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.provider.details.factory.DefaultStationDetailsContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.details.factory.VeloPlusStationDetailsContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.details.factory.VeloVStationDetailsContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.DefaultStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloPlusStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.VeloVStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.DefaultStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.SimpleStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.ui.view.DistanceStationItemRenderer;
import org.helyx.app.j2me.velocite.ui.view.StationListView;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryNotFoundExcepton;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.logging4me.Logger;


public class CartoManager {

	private static final Logger logger = Logger.getLogger("CARTO_MANAGER");
	
	public static final String VELIB = "VELIB";  // PARIS
	public static final String VELO_PLUS = "VELO_PLUS";  // ORLEANS
	public static final String VELO = "VELO";  // TOULOUSE
	public static final String VELO_V = "VELO_V";  // LYON
	public static final String LE_VELO = "LE_VELO";  // MARSEILLE
	public static final String SEVICI = "SEVICI";  // SEVILLE
	
	private static final String DEFAULT_NORMALIZER = "DEFAULT";
	private static final String SIMPLE_NORMALIZER = "SIMPLE";
	private static final String SIMPLE_2_NORMALIZER = "SIMPLE_2";
	
	private CartoManager() {
		super();
	}
	
	public static IProgressTask createUpdateCityStationsTask(City city) throws CartoManagerException {
		
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
			
			IContentProvider cp = cpf.createContentProvider();
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
	
			return progressTask;
		}
		catch (ContentProviderFactoryException e) {
			throw new CartoManagerException(e);
		}
		catch (ContentProviderFactoryNotFoundExcepton e) {
			throw new CartoManagerException(e);
		}

	}

	public static IStationInfoNormalizer getStationInfoNormalizer(City city) throws CartoManagerException {
		if (city.normalizer == null || DEFAULT_NORMALIZER.equals(city.normalizer)) {
			return new DefaultStationInfoNormalizer();
		}
		else if (SIMPLE_NORMALIZER.equals(city.normalizer)) {
			return new SimpleStationInfoNormalizer("-");
		}
		else if (SIMPLE_2_NORMALIZER.equals(city.normalizer)) {
			return new SimpleStationInfoNormalizer("_");
		}
		else {
			throw new CartoManagerException("No StationInfoNormalizer for city: '" + city.type + "' and normalizer: '" + city.normalizer + "'");
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
			else if (VELO_V.equals(city.type)) {
				cpf = new VeloVStationDetailsContentProviderFactory(city, station);
			}
			else if (VELO_PLUS.equals(city.type)) {
				cpf = new VeloPlusStationDetailsContentProviderFactory(city, station);
			}
			else {
				throw new ContentProviderFactoryNotFoundExcepton("No ContentProviderFactory for city type: '" + city.type + "' and key: '" + city.key + "'");
			}
			
			IContentProvider cp = cpf.createContentProvider();
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
	
			return progressTask;
		}
		catch (ContentProviderFactoryException e) {
			throw new CartoManagerException(e);
		}
		catch (ContentProviderFactoryNotFoundExcepton e) {
			throw new CartoManagerException(e);
		}
	}

	public static void cleanUpData() {
		StationPersistenceService stationPersistenceService = new StationPersistenceService();
		try {
			stationPersistenceService.removeAllStations();
		}
		finally {
			stationPersistenceService.dispose();
		}	
	}

	public static void showStationByDistance(AbstractDisplayable displayable, AbstractDisplayable previousDisplayable, final Station station, final int distanceMax, boolean recordFilterEnabled, boolean allowMenu, boolean allowNested) {
		StationListView stationListView = new StationListView(displayable.getMidlet(), "Station Proches");
		stationListView.setReferentStation(station);
		stationListView.setAllowMenu(allowMenu);
		stationListView.setPreviousDisplayable(previousDisplayable);
		stationListView.setCellRenderer(new DistanceStationItemRenderer());
		stationListView.setRecordFilterEnabled(recordFilterEnabled);
		stationListView.setAllowNested(allowNested);
		
		UIStationLoaderProgressListener slpl = new UIStationLoaderProgressListener(
				stationListView, 
				new StationDistanceFilter(station, distanceMax));
		stationListView.loadListContent(slpl);
	}
	
}
