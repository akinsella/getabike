package org.helyx.app.j2me.velocite.data.carto.manager;

import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.content.provider.IContentProviderFactory;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.ui.displayable.IAbstractDisplayable;
import org.helyx.app.j2me.velocite.data.carto.listener.StationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.DefaultStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.LyonStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.OrleansStationContentProviderFactory;
import org.helyx.app.j2me.velocite.data.carto.provider.factory.StationContentProviderFactoryNoFoundExcepton;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class CartoManager {

	private static final String CAT = "CARTO_MANAGER";
	
	private static final String DEFAULT = "DEFAULT";
	private static final String LYON = "LYON";
	private static final String ORLEANS = "ORLEANS";
	
	private CartoManager() {
		super();
	}
	
	public static void refreshAll(City city, final MIDlet midlet, final IAbstractDisplayable currentDisplayable, final IAbstractDisplayable targetDisplayable) throws CartoManagerException {

		try {
			IContentProviderFactory cpf = null;
			if (DEFAULT.equals(city.contentProviderFactory)) {
				cpf = new DefaultStationContentProviderFactory();
			}
			else if (LYON.equals(city.contentProviderFactory)) {
				cpf = new LyonStationContentProviderFactory();
			}
			else if (ORLEANS.equals(city.contentProviderFactory)) {
				cpf = new OrleansStationContentProviderFactory();
			}
			else {
				throw new StationContentProviderFactoryNoFoundExcepton("ContentProviderFactory searched for city key: '" + city.key + "'");
			}
	
			
			IContentProvider cp = cpf.getContentProviderFactory(city);
			
			IProgressTask progressTask = new ContentProviderProgressTaskAdapter(cp);
			progressTask.addProgressListener(new StationLoaderProgressListener(progressTask.getProgressDispatcher()));
	
			TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, midlet, currentDisplayable, targetDisplayable);
		}
		catch (StationContentProviderFactoryNoFoundExcepton e) {
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
