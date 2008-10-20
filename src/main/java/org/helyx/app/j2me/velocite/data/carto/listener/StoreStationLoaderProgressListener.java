package org.helyx.app.j2me.velocite.data.carto.listener;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.sort.FastQuickSort;
import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.comparator.StationNameComparator;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;

public class StoreStationLoaderProgressListener extends ProgressAdapter {
	
	private static final Log log = LogFactory.getLog("STATION_LOADER_PROGRESS_LISTENER");

	private IStationPersistenceService stationPersistenceService;
	
	private Vector stationList;
	private Station[] stationArray;
	private IProgressDispatcher progressDispatcher;

	public StoreStationLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super();
		this.progressDispatcher = progressDispatcher;
		this.stationList = new Vector(4096);
	}

	public void onStart(String eventMessage, Object eventData) {
		stationPersistenceService = new StationPersistenceService();
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Suppression des stations");
		stationPersistenceService.removeAllStations();
	}

	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == CartoConstants.ON_STATION_LOADED) {
			Station station = (Station)eventData;
			stationList.addElement(station);
			int size = stationList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, stationList.size() + " stations chargées");
			}
		}
	}
	
	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
   		try {
   			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, stationList.size() + " stations chargées");
	 		stationArray = new Station[stationList.size()];
			stationList.copyInto(stationArray);
			stationList = null;
			System.gc();
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Tri des données");
			try { new FastQuickSort(new StationNameComparator()).sort(stationArray); } catch (Exception e) { log.warn(e); }
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Sauvegarde des stations");
			stationPersistenceService.saveStationArray(stationArray);
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Chargement terminé");
			stationArray = null;
			System.gc();
   		}
   		finally {
   			stationPersistenceService.dispose();
   		}
	}

}