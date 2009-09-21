package org.helyx.app.j2me.velocite.data.carto.listener;

import java.util.Vector;

import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.comparator.StationNameComparator;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.helyx4me.sort.FastQuickSort;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressDispatcher;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.logging4me.Logger;


public class StoreStationLoaderProgressListener extends ProgressAdapter {
	
	private static final Logger logger = Logger.getLogger("STORE_STATION_LOADER_PROGRESS_LISTENER");

	private IStationPersistenceService stationPersistenceService;
	
	private Vector stationList;
	private Station[] stationArray;
	private IProgressDispatcher progressDispatcher;

	public StoreStationLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super(logger.getCategory().getName());
		this.progressDispatcher = progressDispatcher;
		this.stationList = new Vector(4096);
	}

	public void onStart(String eventMessage, Object eventData) {
		stationPersistenceService = new StationPersistenceService();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Suppression des stations");
		stationPersistenceService.removeAllStations();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Chargement des stations");
	}

	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == CartoConstants.ON_STATION_LOADED) {
			Station station = (Station)eventData;
			stationList.addElement(station);
			int size = stationList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, stationList.size() + " stations chargées");
			}
		}
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
   		try {
   			progressDispatcher.fireEvent(EventType.ON_PROGRESS, stationList.size() + " stations chargées");
	 		stationArray = new Station[stationList.size()];
			stationList.copyInto(stationArray);
			stationList = null;
			System.gc();
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Tri des données");
			try { new FastQuickSort(new StationNameComparator()).sort(stationArray); } catch (Exception e) { logger.warn(e); }
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Sauvegarde des stations");
			stationPersistenceService.saveStationArray(stationArray);
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Chargement terminé");
			stationArray = null;
			System.gc();
   		}
   		finally {
   			stationPersistenceService.dispose();
   		}
	}

}