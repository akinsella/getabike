package org.helyx.app.j2me.getabike.data.carto.task;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.getabike.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.getabike.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.logging4me.Logger;



public class StationLoadTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("STATION_LOAD_TASK");

	private RecordFilter recordFilter;
	private StationListView stationListView;
		
	public StationLoadTask(StationListView stationListView) {
		super(logger.getCategory().getName());
		this.stationListView = stationListView;
	}

	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				System.gc();
				IStationPersistenceService stationPersistenceService = null;
				MultiRecordEnumeration stationEnumeration = null;
				Station[] stationArray = new Station[0];
				try {
					try {
						progressDispatcher.fireEvent(EventType.ON_START);
						
						progressDispatcher.fireEvent(EventType.ON_PROGRESS, stationListView.getMessage("task.station.load.data.read"));
			
						stationPersistenceService = new StationPersistenceService();
				
						Vector stationList = new Vector(4096);
						
						stationEnumeration = stationPersistenceService.createStationEnumeration(null);
			
						int count = 0;
						while (stationEnumeration.hasMoreElements()) {
			
							Station station = (Station)stationEnumeration.nextElement();
							count++;
							if (count % 5 == 0) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, count + " " + stationListView.getMessage("task.station.load.data.loaded"));
							}
			
							stationList.addElement(station);
						}
			
						
						progressDispatcher.fireEvent(EventType.ON_PROGRESS, count + " " + stationListView.getMessage("task.station.load.data.loaded"));
				
						if (logger.isInfoEnabled()) {
							logger.info("Copying Station Array ...");
						}
						stationArray = new Station[stationList.size()];
						stationList.copyInto(stationArray);
						stationList = null;
						System.gc();
					}
			    	finally {
			    		if (stationEnumeration != null) {
			    			stationEnumeration.destroy();
			    		}
			    		if (stationPersistenceService != null) {
			    			stationPersistenceService.dispose();
			    		}
			    	}
					progressDispatcher.fireEvent(EventType.ON_SUCCESS, stationArray);
					stationArray = null;
					System.gc();
				}
				catch(Throwable t) {
					logger.warn(t);
					progressDispatcher.fireEvent(EventType.ON_ERROR, t);
				}
				
			}
			
		};
	
	}

	public boolean isCancellable() {
		return false;
	}
	
}
