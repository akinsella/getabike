package org.helyx.app.j2me.velocite.data.carto.manager;

import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.ui.displayable.IAbstractDisplayable;
import org.helyx.app.j2me.velocite.data.carto.listener.StationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class CartoManager {

	private static final String CAT = "CARTO_MANAGER";
	
	private CartoManager() {
		super();
	}
	
	public static void refreshAll(City city, final MIDlet midlet, final IAbstractDisplayable currentDisplayable, final IAbstractDisplayable targetDisplayable) throws CartoManagerException {
		
		try {
			Class cpfClass = Class.forName(city.contentProviderFactory);
			
			IContentProviderFactory cpf = (IContentProviderFactory)cpfClass.newInstance();
			IContentProvider cp = cpf.getContentProviderFactory(city);
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
			progressTask.addProgressListener(new StationLoaderProgressListener(progressTask.getProgressDispatcher()));
	
			TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, midlet, currentDisplayable, targetDisplayable);
		}
		catch(ClassNotFoundException e) {
			throw new CartoManagerException(e);
		}
		catch (InstantiationException e) {
			throw new CartoManagerException(e);
		}
		catch (IllegalAccessException e) {
			throw new CartoManagerException(e);
		}
		
	}

	public static void cleanUpSavedData() {
		StationPersistenceService stationPersistenceService = new StationPersistenceService();
		try {
			stationPersistenceService.removeAllStations();
		}
		finally {
			stationPersistenceService.dispose();
		}	
	}
	
}
