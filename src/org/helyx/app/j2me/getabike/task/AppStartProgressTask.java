package org.helyx.app.j2me.getabike.task;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.listener.CityLoaderProgressListener;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.data.language.listener.LanguageLoaderProgressListener;
import org.helyx.app.j2me.getabike.data.language.manager.LanguageManager;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.uuid.UUID;
import org.helyx.logging4me.Logger;


public class AppStartProgressTask extends AbstractProgressTask {

	private static final Logger logger = Logger.getLogger("APP_START_PROGRESS_TASK");
	
	private static final int STEP_START = 1;
	private static final int STEP_CITY_DATA_OK = 3;
	private static final int STEP_LANGUAGE_DATA_OK = 4;
	private static final int STEP_SOFTKEY_DATA_OK = 5;

	private AbstractView view;
	
	private String appUuidStr;
	
	public AppStartProgressTask(AbstractView view) {
		super(view.getMessage("task.app.start.title"));
		this.view = view;
	}
	
	public Runnable getRunnable() {
		return new Runnable() {
		
			public void run() {
				try {
					AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_START);			
					executeStartUpTasks();
				}
				catch(Throwable t) {
					AppStartProgressTask.this.progressDispatcher.fireEvent(EventType.ON_ERROR, t.getMessage(), t);			
				}
			}
		
		};
	}
	
	private void executeStartUpTasks() {

		readAppUuidValue();
		
		checkApplicationDataCleanUpNeeded();
		
		checkCityDataCleanUpNeeded();

		checkAppRun();
		
	}
	
	private void checkAppRun() {
		String currentVersion = PrefManager.readPrefString(PrefConstants.APP_VERSION);
		String newVersion = view.getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
		if (currentVersion == null) {
			onFirstRun(newVersion);
		}
		else if (!currentVersion.equals(newVersion)) {
			onAppUpdate(currentVersion, newVersion);
		}
		else {
			onNormalRun(currentVersion);
		}
	}

	private void readAppUuidValue() {
		appUuidStr = PrefManager.readPrefString(PrefConstants.APP_UUID);
		
		if (logger.isInfoEnabled()) {
			logger.info("Actual App UUID: '" + appUuidStr + "'");
		}
	}

	private void checkApplicationDataCleanUpNeeded() {
		
		boolean applicationDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED);
		if (applicationDataCleanUpNeeded) {
			onProgress(view.getMessage("task.app.start.data.remove.start"));
			
			if (logger.isInfoEnabled()) {
				logger.info("Application data need to be reseted");
			}

			if (logger.isInfoEnabled()) {
				logger.info("Cleaning up cities related data");
			}
			CityManager.clearCities();
			
			if (logger.isInfoEnabled()) {
				logger.info("Cleaning up station related data");
			}
			CartoManager.clearStations();
			
			if (logger.isInfoEnabled()) {
				logger.info("Cleaning up language related data");
			}
			LanguageManager.cleanUpSavedData();
			
			if (logger.isInfoEnabled()) {
				logger.info("Cleaning up preference related data");
			}
			PrefManager.cleanUpSavedData();
			initPrefValues();
			
			onProgress(view.getMessage("task.app.start.data.remove.end"));
		}
	}

	private void checkCityDataCleanUpNeeded() {
		boolean cityDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
		if (cityDataCleanUpNeeded) {
			onProgress(view.getMessage("task.app.start.data.city.remove.start"));
			if (logger.isInfoEnabled()) {
				logger.info("City data need to be reseted");
				logger.info("Cleaning up cities related data");				
			}
			CityManager.clearCurrentCountry();
			CityManager.clearCurrentCity(true);
			CityManager.clearCities();
			PrefManager.removePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
			onProgress(view.getMessage("task.app.start.data.city.remove.end"));
		}
	}

	private void initPrefValues() {
		initGoogleMapsKeyValue();
		initAppUuidValue();
		initAppKey();
	}

	private void initAppKey() {
		PrefManager.writePref(PrefConstants.APP_KEY, view.getMidlet().getAppKey());
	}
	
	private void initGoogleMapsKeyValue() {
		PrefManager.writePref(UtilManager.GOOGLE_MAPS_KEY, UtilManager.DEFAULT_GOOGLE_MAPS_KEY);
	}
	
	private void initAppUuidValue() {
		if (appUuidStr == null || appUuidStr.trim().length() == 0) {
			UUID appUuid = new UUID();
			appUuidStr = appUuid.toString();
		}
		
		PrefManager.writePref(PrefConstants.APP_UUID, appUuidStr);
		
		if (logger.isInfoEnabled()) {
			logger.info("App UUID: '" + appUuidStr + "'");
		}
	}

	private void onAppUpdate(String oldVersion, String newVersion) {
		if (logger.isInfoEnabled()) {
			logger.info("This is  an update of an older version: '" + oldVersion + "'");
			logger.info("New version is: '" + newVersion + "'");

			logger.info("Old version is different from new Version");
		}
		updateRunStats();
		updateVersion(newVersion);
		
		checkData(STEP_START);
	}

	private void onFirstRun(String newVersion) {
		initPrefValues();
		updateRunStats();
		updateVersion(newVersion);
		if (logger.isInfoEnabled()) {
			logger.info("This is not an update of an older version. New version is: '" + newVersion + "'");
		}
		checkData(STEP_START);
	}

	private void onNormalRun(String currentVersion) {
		if (logger.isInfoEnabled()) {
			logger.info("This is a normal run. Current version is: '" + currentVersion + "'");
		}
		updateRunStats();
		checkData(STEP_START);
	}
	
	private void updateRunStats() {
		updateRunCount();
		updateRunTimeStamp();
	}

	private void updateRunCount() {
		int runCount = 0;
		try { runCount = Integer.parseInt(PrefManager.readPrefString(PrefConstants.RUN_COUNT)); } catch(Throwable t) { logger.info("No 'RUN_COUNT' information"); }
		PrefManager.writePref(PrefConstants.RUN_COUNT, String.valueOf(runCount + 1));
	}

	private void updateRunTimeStamp() {
		long lastRunTimestamp = 0;
		try { lastRunTimestamp = Long.parseLong(PrefManager.readPrefString(PrefConstants.LAST_RUN_TIMESTAMP)); } catch(Throwable t) { logger.info("No 'RUN_COUNT' information"); }
		PrefManager.writePref(PrefConstants.LAST_RUN_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
		PrefManager.writePref(PrefConstants.PREVIOUS_RUN_TIMESTAMP, String.valueOf(lastRunTimestamp));
	}

	private void updateVersion(String newVersion) {
		PrefManager.writePref(PrefConstants.APP_VERSION, newVersion);
	}
	
	private void checkData(int step) {
		switch(step) {
			case STEP_START:
				checkLanguages();
				break;
			case STEP_LANGUAGE_DATA_OK:
				checkCities();
				break;
			case STEP_CITY_DATA_OK:
				checkSoftKeys();
				break;
			case STEP_SOFTKEY_DATA_OK:
				onSuccess();
				break;
			default:
				onError(view.getMessage("task.app.start.error.unexpected.step"), null);
				break;
		}
	}
	
	private void checkLanguages() {
		int languagagesCount = LanguageManager.countLanguages();
		if (languagagesCount <= 0) {
			configureLanguages();
		}
		else {
			checkData(STEP_LANGUAGE_DATA_OK);
		}
	}
	
	private void checkCities() {
		int cityCount = CityManager.countCities();
		if (cityCount <= 0) {
			configureCities();
		}
		else {
			checkData(STEP_CITY_DATA_OK);
		}
	}
	
	private void checkSoftKeys() {
		configureSoftKeys();
	}

	private void configureCities() {
		onProgress(view.getMessage("task.app.start.data.city.load.start"));
		IProgressTask progressTask = CityManager.createUpdateCitiesTask();
		progressTask.addProgressListener(new CityLoaderProgressListener(progressTask.getProgressDispatcher(), view));
		progressTask.addProgressListener(new StartProgressAdapter(STEP_CITY_DATA_OK));
		progressTask.execute();
	}
	
	private  void configureLanguages() {
		onProgress(view.getMessage("task.app.start.data.language.load.start"));
		IProgressTask progressTask = LanguageManager.createUpdateLanguagesTask();
		progressTask.addProgressListener(new LanguageLoaderProgressListener(progressTask.getProgressDispatcher(), view));
		progressTask.addProgressListener(new ProgressAdapter("LOCALE_CONFIGURER") {
			public void onSuccess(String eventMessage, Object eventData) {
				LanguageManager.configureLocaleWithCurentLanguage(view.getMidlet());
			}
		});
		progressTask.addProgressListener(new StartProgressAdapter(STEP_LANGUAGE_DATA_OK));
		progressTask.execute();
	}
	
	private void configureSoftKeys() {
		onProgress(view.getMessage("task.app.start.data.softkey.load.start"));
		IProgressTask progressTask = new SoftKeyConfigurationTask(view);
		progressTask.addProgressListener(new StartProgressAdapter(STEP_SOFTKEY_DATA_OK));
		progressTask.execute();
	}
	
	private class StartProgressAdapter extends ProgressAdapter {
		
		private int successStep;
		
		public StartProgressAdapter(int succesStep) {
			super(AppStartProgressTask.logger.getCategory().getName());
			this.successStep = succesStep;
		}

		public void onSuccess(String eventMessage, Object eventData) {
			try {
				checkData(successStep);
			}
			catch(Throwable t) {
				AppStartProgressTask.this.logger.warn(t);
				AppStartProgressTask.this.onError(t.getMessage(), t);
			}
		}
		
		public void onError(String eventMessage, Object eventData) {
			AppStartProgressTask.this.onError(eventMessage, eventData);
		}
		
		public void onProgress(String eventMessage, Object eventData) {
			if (AppStartProgressTask.this.logger.isDebugEnabled()) {
				AppStartProgressTask.this.logger.debug(eventData);				
			}
//			AppStartProgressTask.this.onProgress(eventMessage);
		}
		
	}
	
	private void onSuccess() {
		progressDispatcher.fireEvent(EventType.ON_SUCCESS);
	}
		
	private void onProgress(String eventMessage) {
		progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage);
	}
	
	private void onProgress(String eventMessage, Object eventData) {
		progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage, eventData);
	}
	
	private void onError(String eventMessage, Object eventData) {
		progressDispatcher.fireEvent(EventType.ON_ERROR, eventMessage, eventData);
	}

}
