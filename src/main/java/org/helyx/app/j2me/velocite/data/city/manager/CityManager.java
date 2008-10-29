package org.helyx.app.j2me.velocite.data.city.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;
import org.helyx.app.j2me.velocite.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.velocite.ui.view.CityListView;

public class CityManager {

	private static final Log log = LogFactory.getLog("CITY_MANAGER");
	
	private static final String CITIES_URL = "http://m.velocite.org/cities/v2/data.xml";
	
	private CityManager() {
		super();
	}

	public static IProgressTask refreshDataWithDefaults() {
		
		IContentAccessor cityContentAccessor = new HttpContentAccessor(CITIES_URL);
		IContentProvider contentProvider = new DefaultCityContentProvider(cityContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

	public static City findSelectedCity() throws CityManagerException {
		Vector cityList = findAllCities();
		City selectedCity = findSelectedCity(cityList);
		
		return selectedCity;
	}
	
	public static City findSelectedCity(Vector cityList) {
		City selectedCity = null;

		String citySelectedKeyPrefValue = PrefManager.readPrefString(PrefConstants.CITY_SELECTED_KEY);
		log.info("Selected City key: " + citySelectedKeyPrefValue);
		String cityDefaultKeyPrefValue = PrefManager.readPrefString(PrefConstants.CITY_DEFAULT_KEY);
		log.info("Default City key: " + citySelectedKeyPrefValue);
		
		Enumeration _enum = cityList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			if (city.key.equals(citySelectedKeyPrefValue)) {
				selectedCity = city;
				break;
			}
		}

		if (selectedCity == null) {
			_enum = cityList.elements();
			while(_enum.hasMoreElements()) {
				City city = (City)_enum.nextElement();
				if (city.active && city.key.equals(cityDefaultKeyPrefValue)) {
					selectedCity = city;
					PrefManager.writePref(PrefConstants.CITY_SELECTED_KEY, selectedCity.key);
					break;
				}
			}
		}
		
		if (selectedCity == null) {
			log.debug("No Selected city");
		}
		else {
			log.debug("Selected city: " + selectedCity);
		}

		
		return selectedCity;
	}

	public static Vector findAllCities() {
		log.debug("Loading all cities ...");
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			Vector cityList = cityPersistenceService.findAllCities();
			
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

	public static void saveSelectedCity(City city) {
		PrefManager.writePref(PrefConstants.CITY_SELECTED_KEY, city.key);
	}
	
	public static void cleanUpSavedData() {
		PrefManager.removePref(PrefConstants.CITY_DEFAULT_KEY);
		PrefManager.removePref(PrefConstants.CITY_SELECTED_KEY);
		CityPersistenceService cityPersistenceService = new CityPersistenceService();
		try {
			cityPersistenceService.removeAllCities();
		}
		finally {
			cityPersistenceService.dispose();
		}
	}
	
	public static void ShowCityListView(AbstractDisplayable abstractDisplayable, AbstractDisplayable previousDisplayable) {
		CityListView cityListView;
		try {
			cityListView = new CityListView(abstractDisplayable.getMidlet());
			cityListView.setPreviousDisplayable(previousDisplayable);
			abstractDisplayable.showDisplayable(cityListView);
		}
		catch (CityManagerException e) {
			log.warn(e);
			abstractDisplayable.showAlertMessage("Probl�me de configuration", "Le fichier des villes n'est pas valide: " + e.getMessage());
		}
	}
	
	public static void ShowCityListView(AbstractDisplayable abstractDisplayable, IReturnCallback returnCallback) {
		CityListView cityListView;
		try {
			cityListView = new CityListView(abstractDisplayable.getMidlet());
			cityListView.setReturnCallback(returnCallback);
		}
		catch (CityManagerException e) {
			log.warn(e);
			abstractDisplayable.showAlertMessage("Probl�me de configuration", "Le fichier des villes n'est pas valide: " + e.getMessage());
		}
	}
	
}
