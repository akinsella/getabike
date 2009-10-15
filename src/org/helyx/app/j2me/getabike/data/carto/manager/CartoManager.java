package org.helyx.app.j2me.getabike.data.carto.manager;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.carto.comparator.StationDistanceComparator;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.filter.StationCityFilter;
import org.helyx.app.j2me.getabike.data.carto.filter.StationDistanceFilter;
import org.helyx.app.j2me.getabike.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.getabike.data.carto.filter.StationZipCodeFilter;
import org.helyx.app.j2me.getabike.data.carto.provider.details.factory.VelibStationDetailsContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.details.factory.VeloPlusStationDetailsContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.details.factory.VeloVStationDetailsContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.factory.CityBikeStationContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.factory.VelibStationContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.factory.VeloPlusStationContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.factory.VeloStationContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.factory.VeloVStationContentProviderFactory;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.CergyStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.DefaultStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.SimpleStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.VelibStationInfoNormalizer;
import org.helyx.app.j2me.getabike.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.ui.view.renderer.DistanceStationItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.renderer.StationItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryNotFoundExcepton;
import org.helyx.helyx4me.filter.ChainedFilter;
import org.helyx.helyx4me.filter.Filter;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.helyx4me.filter.FilterHelper;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.logging4me.Logger;


public class CartoManager {

	private static final Logger logger = Logger.getLogger("CARTO_MANAGER");
	
	public static final String VELIB = "VELIB";  // PARIS
	public static final String VELO_PLUS = "VELO_PLUS";  // ORLEANS
	public static final String VELO = "VELO";  // TOULOUSE
	public static final String VELO_V = "VELO_V";  // LYON
	public static final String CITYBIKE = "CITYBIKE";  // VIENNE
	
	private static final String DEFAULT_NORMALIZER = "DEFAULT";
	private static final String SIMPLE_NORMALIZER = "SIMPLE";
	private static final String SIMPLE_UNDERSCORE_NORMALIZER = "SIMPLE_U";
	private static final String VELIB_NORMALIZER = "VELIB";
	private static final String CERGY_NORMALIZER = "CERGY";
	
	private CartoManager() {
		super();
	}
	
	public static IProgressTask createUpdateCityStationsTask(City city) throws CartoManagerException {
		
		try {
			IContentProviderFactory cpf = null;
			if (VELIB.equals(city.type)) {
				cpf = new VelibStationContentProviderFactory(city);
			}
			else if (VELO.equals(city.type)) {
				cpf = new VeloStationContentProviderFactory(city);
			}
			else if (VELO_V.equals(city.type)) {
				cpf = new VeloVStationContentProviderFactory(city);
			}
			else if (VELO_PLUS.equals(city.type)) {
				cpf = new VeloPlusStationContentProviderFactory(city);
			}
			else if (CITYBIKE.equals(city.type)) {
				cpf = new CityBikeStationContentProviderFactory(city);
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
		String cityNormalizer = city.normalizer;
		if (cityNormalizer == null) {
			return new DefaultStationInfoNormalizer();
		}
		else if (DEFAULT_NORMALIZER.equals(cityNormalizer)) {
			return new DefaultStationInfoNormalizer();
		}
		else if (SIMPLE_NORMALIZER.equals(cityNormalizer)) {
			return new SimpleStationInfoNormalizer();
		}
		else if (SIMPLE_UNDERSCORE_NORMALIZER.equals(cityNormalizer)) {
			return new SimpleStationInfoNormalizer('_');
		}
		else if (VELIB_NORMALIZER.equals(cityNormalizer)) {
			return new VelibStationInfoNormalizer();
		}
		else if (CERGY_NORMALIZER.equals(cityNormalizer)) {
			return new CergyStationInfoNormalizer();
			}
		else {
			throw new CartoManagerException("No StationInfoNormalizer for city: '" + city.type + "' and normalizer: '" + cityNormalizer + "'");
		}
	}

	public static IProgressTask fetchStationDetails(City city, Station station) throws CartoManagerException {

		try {
			IContentProviderFactory cpf = null;
			if (VELIB.equals(city.type)) {
				cpf = new VelibStationDetailsContentProviderFactory(city, station);
			}
			else if (VELO.equals(city.type)) {
				cpf = new VelibStationDetailsContentProviderFactory(city, station);
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

	public static void clearStations() {
		StationPersistenceService stationPersistenceService = new StationPersistenceService();
		try {
			stationPersistenceService.removeAllStations();
		}
		finally {
			stationPersistenceService.dispose();
		}	
	}

	public static void showStationByDistance(AbstractDisplayable previousDisplayable, final City city, final Station station, final int distanceMax, boolean filteringEnabled, boolean allowMenu, boolean allowNested) {
		StationListView stationListView = new StationListView(previousDisplayable.getMidlet(), "manager.carto.view.station.list.title");
		stationListView.setCity(city);
		stationListView.setReferentStation(station);
		stationListView.setAllowMenu(allowMenu);
		stationListView.setPreviousDisplayable(previousDisplayable);
		stationListView.setCellRenderer(new DistanceStationItemRenderer());
		stationListView.setAllowNested(allowNested);
		stationListView.setFilterBuilder(FilterHelper.createFilterBuilder(new StationDistanceFilter(station, distanceMax)));
		stationListView.setComparator(new StationDistanceComparator());
		stationListView.loadListContent();
	}

	public static void showStationListView(AbstractDisplayable previousDisplayable, final City city) {
		StationListView stationListView = new StationListView(previousDisplayable.getMidlet(), "view.station.list.title");
		stationListView.setPreviousDisplayable(previousDisplayable);
		stationListView.setCellRenderer(new StationItemRenderer());
		stationListView.setCity(city);
		stationListView.setAllowMenu(true);
		stationListView.setAllowNested(true);
		stationListView.setFilterBuilder(new FilterBuilder() {
			
			public Filter buildFilter() {
				ChainedFilter chainedFilter = new ChainedFilter();
				
				String stationNameFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_NAME_FILTER);
				if (stationNameFilter != null && stationNameFilter.length() > 0) {
					chainedFilter.addFilter(new StationNameFilter(stationNameFilter));
				}

				String zipCodeFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_ZIPCODE_FILTER);
				if (zipCodeFilter != null && zipCodeFilter.length() > 0) {
					chainedFilter.addFilter(new StationZipCodeFilter(zipCodeFilter));
				}

				String cityFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_CITY_FILTER);
				if (cityFilter != null && cityFilter.length() > 0) {
					chainedFilter.addFilter(new StationCityFilter(cityFilter));
				}

				return chainedFilter;
			}
		});
		stationListView.loadListContent();
	}
	
}
