package org.helyx.app.j2me.getabike.data.city.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.content.accessor.HttpGetABikeContentAccessor;
import org.helyx.app.j2me.getabike.data.app.manager.AppManager;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.provider.DefaultCityContentProvider;
import org.helyx.app.j2me.getabike.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.getabike.ui.view.CityListView;
import org.helyx.app.j2me.getabike.ui.view.CountryListView;
import org.helyx.helyx4me.cache.Cache;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.Pref;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;

public class CityManager {

	private static final Logger logger = Logger.getLogger("CITY_MANAGER");
	
	private static final String CITY_LIST = "city.list";
	private static final String DATA_CITY_URL = "data.city.url";
	
	private static Cache cache = new Cache();
	
	private CityManager() {
		super();
	}

	public static IProgressTask createUpdateCitiesTask() {
		String cityDataUrl = AppManager.getProperty(DATA_CITY_URL);
		IContentAccessor cityContentAccessor = new HttpGetABikeContentAccessor(cityDataUrl);
		IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

//	public static IProgressTask createCheckUpdateCitiesTask() {
//		ApplicationMetaData applicationMetaData = AppManager.getMetadata(true);
//		IContentAccessor dataCitiesContentAccessor = new HttpGetABikeContentAccessor(DATA_PROPERTIES_URL);
//		IContentProvider contentProvider = new PropertiesContentProvider(dataCitiesContentAccessor);
//		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);
//
//		return progressTask;
//	}

	public static City getCurrentCity() {
		Vector cityList = findAllCities();
		
		City selectedCity = null;

		Pref citySelectedKeyPref = PrefManager.readPref(PrefConstants.CITY_CURRENT_KEY);
		if (citySelectedKeyPref == null) {
			return null;
		}
		
		String citySelectedKeyPrefValue = citySelectedKeyPref.value;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Current City key: " + citySelectedKeyPrefValue);
		}
		
		Enumeration _enum = cityList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			if (city.key.equals(citySelectedKeyPrefValue)) {
				selectedCity = city;
				break;
			}
		}
		
		if (selectedCity == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No Current city");
			}
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Current city: " + selectedCity);
			}
		}

		
		return selectedCity;
	}

	public static Vector findAllCities() {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading all cities ...");
		}
		Vector cityList = (Vector)cache.get(CITY_LIST);
		if (cityList != null) {
			return cityList;
		}
		
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			cityList = cityPersistenceService.findAllCities();
			cache.set(CITY_LIST, cityList);
			return cityList;
		}
		finally {
			cityPersistenceService.dispose();
		}
	}

	public static Vector findAllCountries() {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading all countries ...");
		}
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			Vector countryList = cityPersistenceService.findAllCountries();
			
			return countryList;
		}
		finally {
			cityPersistenceService.dispose();
		}
	}

	public static Vector findCitiesByCountryName(String countryName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading all cities for country: '" + countryName + "'...");
		}
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			Vector cityList = cityPersistenceService.findAllCitiesByCountryName(countryName);
			
			return cityList;
		}
		finally {
			cityPersistenceService.dispose();
		}
	}

	public static int countCities() {
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			int count = cityPersistenceService.countCities();
			
			return count;
		}
		finally {
			cityPersistenceService.dispose();
		}
	}

	public static int countCitiesByCountryName(String countryName) {
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			int count = cityPersistenceService.countCitiesByCountryName(countryName);
			
			return count;
		}
		finally {
			cityPersistenceService.dispose();
		}
	}
	
	public static void clearCities() {
		cache.remove(CITY_LIST);
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			cityPersistenceService.removeAllCities();
			
		}
		finally {
			cityPersistenceService.dispose();
		}
	}

	public static void clearCurrentCountry() {
		PrefManager.removePref(PrefConstants.COUNTRY_SELECTED_KEY);
	}

	public static String getCurrentCountry() {
		String currentCountry = PrefManager.readPrefString(PrefConstants.COUNTRY_SELECTED_KEY);
		return currentCountry;
	}

	public static void setCurrentCountry(String country) {
		PrefManager.writePref(PrefConstants.COUNTRY_SELECTED_KEY, country);
	}

	public static void clearCurrentCity(boolean clearCityData) {
		PrefManager.removePref(PrefConstants.CITY_CURRENT_KEY);
		if (clearCityData) {
			CartoManager.clearStations();
			clearStationSearch();
		}
	}

	public static void clearStationSearch() {
		PrefManager.removePref(PrefConstants.PREF_STATION_NAME_FILTER);
		PrefManager.removePref(PrefConstants.PREF_STATION_ZIPCODE_FILTER);
		PrefManager.removePref(PrefConstants.PREF_STATION_CITY_FILTER);		
	}

	public static void setCurrentCity(City city) {
		PrefManager.writePref(PrefConstants.CITY_CURRENT_KEY, city.key);
		clearStationSearch();
	}
	
	public static void selectCountry(final AbstractView currentView, final IReturnCallback returnCallback) {
		final CountryListView countryListView = new CountryListView(currentView.getMidlet(), true);
		countryListView.setReturnCallback(new IReturnCallback() {

			public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
				String currentCountry = CityManager.getCurrentCountry();
				returnCallback.onReturn(countryListView, currentCountry != null ? currentCountry : null);
			}
			
		});
		currentView.showDisplayable(countryListView);
	}

	public static void selectCity(AbstractView currentView, IReturnCallback returnCallback) {
		selectCity(currentView, returnCallback, false);
	}

	public static void selectCity(final AbstractView currentView, final IReturnCallback returnCallback, boolean forceLoadCityListView) {
		final City currentCity = CityManager.getCurrentCity();
		if (currentCity != null && !forceLoadCityListView) {
			returnCallback.onReturn(currentView, currentCity);	
			return ;
		}
		
		final String currentCountry = CityManager.getCurrentCountry();
		
		if (currentCountry == null) {
			CityManager.selectCountry(currentView, new IReturnCallback() {
				public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
					String currentCountry = (String)data;
					if (currentCountry != null) {
						selectCity(currentView, returnCallback);
					}
					else {
						returnCallback.onReturn(currentView, null);
					}
				}
			});
			return ;
		}
			
		final CityListView cityListView = new CityListView(currentView.getMidlet(), currentCountry, true);
		cityListView.setReturnCallback(new IReturnCallback() {
			public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
				try {
					City currentCity = CityManager.getCurrentCity();
					
					// Should not append!
					if (currentCity != null && !currentCity.country.equals(currentCountry)) {
						CityManager.clearCurrentCountry();
						CityManager.clearCurrentCity(true);
						returnCallback.onReturn(cityListView, null);
					}
					else {
						returnCallback.onReturn(cityListView, currentCity);
					}
				}
				catch(Throwable t) {
					logger.warn(t);
					returnCallback.onReturn(currentView, null);
				}
			}
		});
		
		currentView.showDisplayable(cityListView);
	}

}
