package org.helyx.app.j2me.velocite.data.city.listener;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.sort.FastQuickSort;
import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.comparator.CityNameComparator;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.velocite.data.city.service.ICityPersistenceService;

public class CityLoaderProgressListener extends ProgressAdapter {

	private static final Log log = LogFactory.getLog("CITY_LOADER_PROGRESS_LISTENER");
	
	private ICityPersistenceService cityPersistenceService;
	
	private Vector cityList;
	private City[] cityArray;
	private IProgressDispatcher progressDispatcher;

	public CityLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super();
		this.progressDispatcher = progressDispatcher;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.cityPersistenceService = new CityPersistenceService();
		this.cityList = new Vector();

   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Suppression des villes ...");
		cityPersistenceService.removeAllCities();
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Chargement des villes ...");
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == CityConstants.ON_CITY_LOADED) {
			onCityLoaded((City)eventData);
		}
		else if (eventType == CityConstants.ON_DEFAULT_CITY) {
			onDefaultCity((String)eventData);
		}
		else if (eventType == CityConstants.ON_CITIES_LOADED) {
			onCitiesLoaded();
		}
	}
	
	private void onCityLoaded(City city) {
		cityList.addElement(city);
		int size = cityList.size();
		
		if (size % 5 == 0) {
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, cityList.size() + " villes chargées");
		}
	}
	
	private void onDefaultCity(String defaultCityKey) {
		if (defaultCityKey == null) {
			PrefManager.removePref(PrefConstants.CITY_DEFAULT_KEY);
		}
		else {
			log.debug("Default city key: '" + defaultCityKey + "'");
			PrefManager.writePref(PrefConstants.CITY_DEFAULT_KEY, defaultCityKey);					
		}
	}

	private void onCitiesLoaded() {
		int cityListSize = cityList.size();
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, cityListSize + " villes chargées");
		cityArray = new City[cityListSize];
		cityList.copyInto(cityArray);

		System.gc();
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Tri des données");
		new FastQuickSort(new CityNameComparator()).sort(cityArray);
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Sauvegarde des villes");
		log.debug("About to save cities");
		
		cityPersistenceService.saveCityArray(cityArray);

   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Chargement terminé");
		log.debug("Cities saved");
		cityArray = null;
		System.gc();
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		log.debug("Disposing cityPersistenceService");
		cityPersistenceService.dispose();
	}

	public void onError(String eventMessage, Object eventData) {
		log.info("Message: " + eventMessage + ", data: " + eventData);
	}

}
