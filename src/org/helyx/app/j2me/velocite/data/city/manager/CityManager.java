package org.helyx.app.j2me.velocite.data.city.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.content.accessor.HttpVelociteContentAccessor;
import org.helyx.app.j2me.velocite.data.app.domain.Version;
import org.helyx.app.j2me.velocite.data.app.manager.AppManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;
import org.helyx.app.j2me.velocite.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.velocite.ui.view.CityListView;
import org.helyx.app.j2me.velocite.ui.view.CountryListView;
import org.helyx.helyx4me.cache.Cache;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.Pref;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.BasicReturnCallback;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
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
		IContentAccessor cityContentAccessor = new HttpVelociteContentAccessor(cityDataUrl);
		IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

//	public static IProgressTask createCheckUpdateCitiesTask() {
//		ApplicationMetaData applicationMetaData = AppManager.getMetadata(true);
//		IContentAccessor dataCitiesContentAccessor = new HttpVelociteContentAccessor(DATA_PROPERTIES_URL);
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
		
		if (logger.isInfoEnabled()) {
			logger.info("Current City key: " + citySelectedKeyPrefValue);
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
	
	public static void showCityListView(AbstractDisplayable currentDisplayable) {
		String currentCountry = CityManager.getCurrentCountry();
		showCityListView(currentDisplayable, currentCountry, new BasicReturnCallback(currentDisplayable));
	}
	
	public static void showCityListView(AbstractDisplayable currentDisplayable, String country) {
		showCityListView(currentDisplayable, country, new BasicReturnCallback(currentDisplayable));
	}
	
	public static void showCityListView(AbstractDisplayable currentDisplayable, IReturnCallback returnCallback) {
		String currentCountry = CityManager.getCurrentCountry();
		showCityListView(currentDisplayable, currentCountry, new BasicReturnCallback(currentDisplayable));
	}
	
	public static void showCityListView(AbstractDisplayable currentDisplayable, String country, IReturnCallback returnCallback) {
		CityListView cityListView;
		try {
			cityListView = new CityListView(currentDisplayable.getMidlet(), country);
			cityListView.setReturnCallback(returnCallback);
			currentDisplayable.showDisplayable(cityListView);
		}
		catch (CityManagerException cme) {
			logger.warn(cme);
			DialogUtil.showAlertMessage(currentDisplayable, currentDisplayable.getMessage("dialog.title.error"), currentDisplayable.getMessage("manage.city.error", cme.getMessage()));
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
		}
	}

	public static void setCurrentCity(City city) {
		PrefManager.writePref(PrefConstants.CITY_CURRENT_KEY, city.key);
	}

	public static void showCountryListView(AbstractDisplayable currentDisplayable) {
		showCountryListView(currentDisplayable, new BasicReturnCallback(currentDisplayable));
	}

	public static void showCountryListView(AbstractDisplayable currentDisplayable, IReturnCallback returnCallback) {
		CountryListView countryListView;
		try {
			countryListView = new CountryListView(currentDisplayable.getMidlet());
			countryListView.setReturnCallback(returnCallback);
			currentDisplayable.showDisplayable(countryListView);
		}
		catch (CityManagerException cme) {
			logger.warn(cme);
			DialogUtil.showAlertMessage(currentDisplayable, currentDisplayable.getMessage("dialog.title.error"), currentDisplayable.getMessage("manage.city.error", cme.getMessage()));
		}
	}
	
}
