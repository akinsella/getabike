package org.helyx.app.j2me.velocite.data.carto.task;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.ui.view.StationListView;


public class StationLoadTask extends AbstractProgressTask {
	
	private static final Log log = LogFactory.getLog("STATION_LOAD_TASK");

	private IRecordFilter recordFilter;
	private StationListView stationListView;
		
	public StationLoadTask(StationListView stationListView, IRecordFilter recordFilter) {
		super(log.getCategory());
		this.stationListView = stationListView;
		this.recordFilter = recordFilter;
	}

	public void execute() {
		System.gc();
		IStationPersistenceService stationPersistenceService = null;
		MultiRecordEnumeration stationEnumeration = null;
		Station[] stationArray = new Station[0];
		try {
			try {
				progressDispatcher.fireEvent(ProgressEventType.ON_START);
				
				progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Lecture des donn�es");
	
				stationPersistenceService = new StationPersistenceService();
		
				Vector stationList = new Vector(4096);
				
				stationEnumeration = stationPersistenceService.createStationEnumeration(recordFilter);
	
				int count = 0;
				while (stationEnumeration.hasMoreElements()) {
	
					Station station = (Station)stationEnumeration.nextElement();
					count++;
					if (count % 5 == 0) {
						progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, count + " stations charg�es");
					}
	
					stationList.addElement(station);
				}
	
				
				progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, count + " stations charg�es");
		
				log.info("Copying Station Array ...");
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
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, stationArray);
			stationArray = null;
			System.gc();
		}
		catch(Throwable t) {
			log.warn(t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}

	}

	public boolean isCancellable() {
		return false;
	}
	
}
