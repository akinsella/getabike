package org.helyx.app.j2me.velocite.task;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.velocite.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.view.StationListView;


public class StationLoadTask extends AbstractProgressTask {
	
	private static final String CAT = "STATION_LOAD_TASK";

	private String stationNameFilter;
	private StationListView stationListView;
		
	public StationLoadTask(StationListView stationListView, String stationNameFilter) {
		super(CAT);
		this.stationListView = stationListView;
		this.stationNameFilter = stationNameFilter;
	}

	public void execute() {
		System.gc();
		IStationPersistenceService stationPersistenceService = null;
		MultiRecordEnumeration stationEnumeration = null;

		try {
			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Lecture des données");

			stationPersistenceService = new StationPersistenceService();
	
			Vector stationList = new Vector(4096);

			
			IRecordFilter stationFilter = null;
			if (stationNameFilter != null && stationNameFilter.length() > 0) {
				stationFilter = new StationNameFilter(stationNameFilter);
			}
			
			stationEnumeration = stationPersistenceService.createStationEnumeration(stationFilter);

			int count = 0;
			while (stationEnumeration.hasMoreElements()) {

				Station station = (Station)stationEnumeration.nextElement();
				count++;
				if (count % 5 == 0) {
					progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, count + " stations chargées");
				}

				stationList.addElement(station);
			}

			
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, count + " stations chargées");
	
			Log.info(CAT, "Copying Station Array ...");
			Station[] stationArray = new Station[stationList.size()];
			stationList.copyInto(stationArray);
			stationList = null;
			System.gc();

			stationListView.setStations(stationArray);
			stationArray = null;
			System.gc();
		}
		catch(Throwable t) {
			Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
    	finally {
    		try {
	    		if (stationEnumeration != null) {
	    			stationEnumeration.destroy();
	    		}
	    		if (stationPersistenceService != null) {
	    			stationPersistenceService.dispose();
	    		}
    		}
    		catch(Throwable t) {
    			Log.warn(CAT, t);
    		}
    		finally {
    			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS);
    		}
    	}

	}

	public boolean isCancellable() {
		return false;
	}
	
}
