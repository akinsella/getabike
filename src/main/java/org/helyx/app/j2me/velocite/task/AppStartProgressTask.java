package org.helyx.app.j2me.velocite.task;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.listener.CityLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.task.LanguageConfigurationTask;
import org.helyx.app.j2me.velocite.midlet.VelociteMIDlet;

public class AppStartProgressTask extends AbstractProgressTask {

	private static final Logger logger = LoggerFactory.getLogger("APP_START_PROGRESS_TASK");
	
	private static final String V_1_0_83 = "1.0.83";
	private static final String V_1_0_82 = "1.0.82";

	private AbstractView view;
	
	public AppStartProgressTask(AbstractView view) {
		super("App Start Task");
		this.view = view;
	}
	
	public void execute() {
		try {
			AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_START);			
			executeStartUpTasks();
		}
		catch(Throwable t) {
			AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_ERROR, t.getMessage(), t);			
		}
	}
	
	private void executeStartUpTasks() {
		boolean applicationDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED);
		if (applicationDataCleanUpNeeded) {
			onProgress("Suppression des données ...");
			
			logger.info("Application data need to be reseted");

			logger.info("Cleaning up cities related data");
			CityManager.cleanUpData();
			
			logger.info("Cleaning up station related data");
			CartoManager.cleanUpData();
			
			logger.info("Cleaning up preference related data");
			PrefManager.cleanUpSavedData();
			
			logger.info("Cleaning up language related data");
			LanguageManager.cleanUpSavedData();
			onProgress("Suppression des données terminée");
		}
		
		boolean cityDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
		if (cityDataCleanUpNeeded) {
			onProgress("Suppression des villes enregistrées ...");
			logger.info("City data need to be reseted");
			logger.info("Cleaning up cities related data");
			CityManager.cleanUpData();
			PrefManager.removePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
			onProgress("Suppression des villes terminée");
		}
		
		String oldVersion = PrefManager.readPrefString(PrefConstants.MIDLET_VERSION);
		String newVersion = view.getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
		if (oldVersion == null) {
			onFirstRun(newVersion);
		}
		else if (!oldVersion.equals(newVersion)) {
			onAppUpdate(oldVersion, newVersion);
		}
		else {
			checkCities();
		}
	}
	
	private void onFirstRun(String newVersion) {
		logger.info("This is not an update of an older version. New version is: '" + newVersion + "'");
		checkCities();
	}
	
	private void onAppUpdate(String oldVersion, String newVersion) {
		logger.info("This is  an update of an older version: '" + oldVersion + "'");
		logger.info("New version is: '" + newVersion + "'");

		logger.info("Old version is different from new Version");
		if (V_1_0_82.equals(newVersion) || V_1_0_83.equals(newVersion)) {
			onProgress("Mise à jour des données ...");
			logger.info("Need to Clean Up Cities to support Lyon City.");
			CityManager.cleanUpData();
		}
		
		checkCities();
	}
	
	private void checkCities() {
		int cityCount = CityManager.countCities();
		if (cityCount <= 0) {
			configureCities();
		}
		else {
			configureLanguages();
		}
	}
	
	private void configureCities() {
		onProgress("Chargement des villes ...");
		IProgressTask progressTask = CityManager.createUpdateCitiesTask();
		progressTask.addProgressListener(new CityLoaderProgressListener(progressTask.getProgressDispatcher()));
		progressTask.addProgressListener(new ProgressAdapter(AppStartProgressTask.logger.getCategory()) {

			public void onSuccess(String eventMessage, Object eventData) {
				configureLanguages();
			}
			
			public void onError(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onError(eventMessage, eventData);
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onProgress(eventMessage, eventData);
			}
			
		});
		progressTask.start();
	}
	
	private  void configureLanguages() {
		onProgress("Chargement des langues ...");
		IProgressTask progressTask = new LanguageConfigurationTask(view.getMidlet(), view.getViewCanvas());
		progressTask.addProgressListener(new ProgressAdapter(AppStartProgressTask.logger.getCategory()) {

			public void onSuccess(String eventMessage, Object eventData) {
				configureSoftKeys();
			}
			
			public void onError(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onError(eventMessage, eventData);
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onProgress(eventMessage, eventData);
			}

		});
		progressTask.start();
	}
	
	private void configureSoftKeys() {
		onProgress("Configuration des touches ...");
		IProgressTask progressTask = new SoftKeyConfigurationTask(view.getViewCanvas());
		progressTask.addProgressListener(new ProgressAdapter(AppStartProgressTask.logger.getCategory()) {

			public void onSuccess(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onSuccess();
			}
			
			public void onError(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onError(eventMessage, eventData);
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				AppStartProgressTask.this.onProgress(eventMessage, eventData);
			}

		});
		progressTask.start();
	}
	
	private void onSuccess() {
		AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_SUCCESS);
	}
		
	private void onProgress(String eventMessage) {
		AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage);
	}
	
	private void onProgress(String eventMessage, Object eventData) {
		AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage, eventData);
	}
	
	private void onError(String eventMessage, Object eventData) {
		AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_ERROR, eventMessage, eventData);
	}

}
