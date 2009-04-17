package org.helyx.app.j2me.velocite.data.city.listener;

import java.util.Vector;

import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.comparator.CityNameComparator;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.velocite.data.city.service.ICityPersistenceService;
import org.helyx.helyx4me.sort.FastQuickSort;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressDispatcher;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;

public class CityLoaderProgressListener extends ProgressAdapter {

	private static final Logger logger = LoggerFactory.getLogger("CITY_LOADER_PROGRESS_LISTENER");
	
	private ICityPersistenceService cityPersistenceService;
	
	private Vector cityList;
	private City[] cityArray;
	private IProgressDispatcher progressDispatcher;

	public CityLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super(logger.getCategory().getName());
		this.progressDispatcher = progressDispatcher;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.cityPersistenceService = new CityPersistenceService();
		this.cityList = new Vector();

   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Suppression des villes ...");
		cityPersistenceService.removeAllCities();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Chargement des villes ...");
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == CityConstants.ON_CITY_LOADED) {
			onCityLoaded((City)eventData);
		}
		else if (eventType == CityConstants.ON_CITIES_LOADED) {
			onCitiesLoaded();
		}
	}
	
	private void onCityLoaded(City city) {
		cityList.addElement(city);
		int size = cityList.size();
		
		if (size % 5 == 0) {
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, cityList.size() + " villes chargées");
		}
	}

	private void onCitiesLoaded() {
		int cityListSize = cityList.size();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, cityListSize + " villes chargées");
		cityArray = new City[cityListSize];
		cityList.copyInto(cityArray);

		System.gc();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Tri des données");
		new FastQuickSort(new CityNameComparator()).sort(cityArray);
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Sauvegarde des villes");
		logger.debug("About to save cities");
		
		cityPersistenceService.saveCityArray(cityArray);

   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Chargement terminé");
		logger.debug("Cities saved");
		cityArray = null;
		System.gc();
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		logger.debug("Disposing cityPersistenceService");
		cityPersistenceService.dispose();
	}

	public void onError(String eventMessage, Object eventData) {
		logger.info("Message: " + eventMessage + ", data: " + eventData);
	}

}
