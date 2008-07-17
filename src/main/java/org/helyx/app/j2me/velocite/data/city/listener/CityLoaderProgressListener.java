package org.helyx.app.j2me.velocite.data.city.listener;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.sort.FastQuickSort;
import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.carto.comparator.CityNameComparator;
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.service.CityPersistenceService;
import org.helyx.app.j2me.velocite.data.city.service.ICityPersistenceService;

public class CityLoaderProgressListener extends ProgressAdapter {

	private static final String CAT = "CITY_LOADER_PROGRESS_LISTENER";
	
	private ICityPersistenceService cityPersistenceService;
	
	private Vector cityList;
	private City[] cityArray;
	private IProgressDispatcher progressDispatcher;

	public CityLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super(CAT + "[" + progressDispatcher.getName() + "]");
		this.progressDispatcher = progressDispatcher;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.cityPersistenceService = new CityPersistenceService();
		this.cityList = new Vector();

//		progressDispatcher.fireEvent(ProgressEventType.ON_START);
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Suppression des villes");
		cityPersistenceService.removeAllCities();
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == CityConstants.ON_CITY_LOADED) {
			City city = (City)eventData;
			cityList.addElement(city);
			int size = cityList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, cityList.size() + " villes chargées");
			}
		}
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		try {
			int cityListSize = cityList.size();
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, cityListSize + " villes chargées");
			cityArray = new City[cityListSize];
			cityList.copyInto(cityArray);

//			cityList = null;
			System.gc();
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Tri des données");
			try { new FastQuickSort(new CityNameComparator()).sort(cityArray); } catch (Exception e) { Log.warn(CAT, e); }
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Sauvegarde des villes");
//			Log.debug("About to save cities");
			cityPersistenceService.saveCityArray(cityArray);
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Chargement terminé");
//			Log.debug("Cities saved");
			cityArray = null;
			System.gc();
	
			// Notify end of progress 
//			progressDispatcher.fireEvent(eventType, eventMessage, eventData);
		}
		finally {
			Log.debug("Disposing cityPersistenceService");
			cityPersistenceService.dispose();
		}
	}

}
