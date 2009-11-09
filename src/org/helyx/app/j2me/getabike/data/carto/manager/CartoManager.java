package org.helyx.app.j2me.getabike.data.carto.manager;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.comparator.StationDistanceComparator;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.filter.DefaultStationFilterBuilder;
import org.helyx.app.j2me.getabike.data.carto.filter.StationDistanceFilter;
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
import org.helyx.app.j2me.getabike.util.ConverterUtil;
import org.helyx.helyx4me.cache.Cache;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.IContentProviderFactory;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryException;
import org.helyx.helyx4me.content.provider.exception.ContentProviderFactoryNotFoundExcepton;
import org.helyx.helyx4me.filter.FilterHelper;
import org.helyx.helyx4me.model.list.IElementProvider;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.text.TextUtil;
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
	
	private static Cache cache = new Cache();

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


	public static void showStationByDistance(AbstractDisplayable previousDisplayable, final City city, IElementProvider stationsProvider, Station referentStation, int distanceMax, boolean allowMenu, boolean allowNested) {
		StationListView stationListView = createStationListViewByDistance(previousDisplayable, city, stationsProvider, referentStation, distanceMax, allowMenu, allowNested);
		
		previousDisplayable.showDisplayable(stationListView);
	}
	
	protected static StationListView createBaseStationListView(AbstractDisplayable previousDisplayable, String title, final City city) {
		StationListView stationListView = new StationListView(previousDisplayable.getMidlet(), title);

		stationListView.setPreviousDisplayable(previousDisplayable);
		stationListView.setCity(city);
		
		return stationListView;
	}

	public static StationListView createStationListView(AbstractDisplayable previousDisplayable, final City city) {
		StationListView stationListView = createBaseStationListView(previousDisplayable, "view.station.list.title", city);
		stationListView.setCellRenderer(new StationItemRenderer());

		stationListView.setFilterBuilder(new DefaultStationFilterBuilder());
		
		return stationListView;
	}

	public static StationListView createStationListViewByDistance(AbstractDisplayable previousDisplayable, City city, IElementProvider stationsProvider, Station referentStation, int distanceMax, boolean allowMenu, boolean allowNested) {
		StationListView stationListView = createBaseStationListView(previousDisplayable, "manager.carto.view.station.list.title", city);

		stationListView.setCellRenderer(new DistanceStationItemRenderer());
		stationListView.setAllowMenu(allowMenu);
		stationListView.setAllowNested(allowNested);
		stationListView.setStationsProvider(stationsProvider);
		stationListView.setReferentStation(referentStation);
		stationListView.setFilterBuilder(FilterHelper.createFilterBuilder(new StationDistanceFilter(referentStation, distanceMax)));
		stationListView.setComparator(new StationDistanceComparator());
		stationListView.filterAndSort();
		
		return stationListView;
	}

	public static boolean isStationNumberBookmarked(City city, Station station) {
		
		int[] stationNumbers = getBookmarkedStationNumbers(city);
		
		int stationNumbersCount = stationNumbers.length;
		
		for (int i = 0 ; i < stationNumbersCount ; i++) {
			if (stationNumbers[i] == station.number) {
				return true;
			}
			
		}
		
		return false;
	}

	public static void addStationNumberToBookmarks(City city, Station station) {
		
		int[] stationNumbers = getBookmarkedStationNumbers(city);
		
		int stationNumbersCount = stationNumbers.length;
		
		StringBuffer sbStationNumbersStr = new StringBuffer();
		
		for (int i = 0 ; i < stationNumbersCount ; i++) {
			if (stationNumbers[i] == station.number) {
				return ;
			}
			
			sbStationNumbersStr.append(stationNumbers[i]).append(";");
		}
		
		sbStationNumbersStr.append(station.number);
		
		String stationNumbersStr = sbStationNumbersStr.toString();
		
		PrefManager.writePref("STATION.BOOKMARK." + city.key, stationNumbersStr);
		
		stationNumbers = convertStationNumberStrToIntArray(stationNumbersStr);
		
		cache.set("STATION.BOOKMARK." + city.key, stationNumbers);
	}

	public static void removeStationNumberFromBookmarks(City city, Station station) {
		
		int[] stationNumbers = getBookmarkedStationNumbers(city);
		
		Vector stationNumberList = ConverterUtil.convertIntArrayToVector(stationNumbers);
		
		
		StringBuffer sbStationNumbersStr = new StringBuffer();
		
		Integer stationNumberInteger = new Integer(station.number);
		if (stationNumberList.contains(stationNumberInteger)) {
			stationNumberList.removeElement(stationNumberInteger);
		}
		
		if (stationNumberList.size() == stationNumbers.length) {
			return ;
		}
		
		stationNumbers = ConverterUtil.convertVectorIntArray(stationNumberList);
		int stationNumbersCount = stationNumbers.length;
		
		for (int i = 0 ; i < stationNumbersCount ; i++) {
			sbStationNumbersStr.append(stationNumbers[i]);
			if (i + 1 < stationNumbersCount) {
				sbStationNumbersStr.append(";");
			}
		}
		
		String stationNumbersStr = sbStationNumbersStr.toString();
		
		PrefManager.writePref("STATION.BOOKMARK." + city.key, stationNumbersStr);
		
		stationNumbers = convertStationNumberStrToIntArray(stationNumbersStr);
		
		cache.set("STATION.BOOKMARK." + city.key, stationNumbers);
	}
	
	public static int[] getBookmarkedStationNumbers(City city) {	
		int[] stationNumbers = (int[])cache.get("STATION.BOOKMARK." + city.key);
		if (stationNumbers != null) {
			return stationNumbers;
		}
		
		String stationNumbersStr = PrefManager.readPrefString("STATION.BOOKMARK." + city.key, null);
		stationNumbers = convertStationNumberStrToIntArray(stationNumbersStr);
		
		return stationNumbers;
	}
	
	private static int[] convertStationNumberStrToIntArray(String stationNumbersStr) {
		if (stationNumbersStr == null) {
			return new int[0];
		}
		
		stationNumbersStr = stationNumbersStr.trim();
		
		if (stationNumbersStr.length() == 0) {
			return new int[0];
		}
		
		String[] stationNumbersStrArray = TextUtil.split(stationNumbersStr, ';');
		
		int stationNumbersCount = stationNumbersStrArray.length;
		int[] stationNumbers = new int[stationNumbersCount];
		
		for (int i = 0 ; i < stationNumbersCount ; i++) {
			int stationNumber = 0;
			try {
				stationNumber = Integer.parseInt(stationNumbersStrArray[i]);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
			stationNumbers[i] = stationNumber;
		}
		
		return stationNumbers;

	}
	
}
