package org.helyx.app.j2me.velocite.data.language.listener;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.sort.FastQuickSort;
import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.data.language.LanguageConstants;
import org.helyx.app.j2me.velocite.data.language.comparator.LanguageNameComparator;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.service.ILanguagePersistenceService;
import org.helyx.app.j2me.velocite.data.language.service.LanguagePersistenceService;

public class LanguageLoaderProgressListener extends ProgressAdapter {

	private static final String CAT = "LANGUAGE_LOADER_PROGRESS_LISTENER";
	
	private ILanguagePersistenceService languagePersistenceService;
	
	private Vector languageList;
	private Language[] languageArray;
	private IProgressDispatcher progressDispatcher;

	public LanguageLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super(CAT + "[" + progressDispatcher.getName() + "]");
		this.progressDispatcher = progressDispatcher;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.languagePersistenceService = new LanguagePersistenceService();
		this.languageList = new Vector();

//		progressDispatcher.fireEvent(ProgressEventType.ON_START);
   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Suppression des langues");
		languagePersistenceService.removeAllCities();
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == LanguageConstants.ON_LANGUAGE_LOADED) {
			Language language = (Language)eventData;
			languageList.addElement(language);
			int size = languageList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, languageList.size() + " langues chargées");
			}
		}
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		try {
			int languageListSize = languageList.size();
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, languageListSize + " langues chargées");
			languageArray = new Language[languageListSize];
			languageList.copyInto(languageArray);

//			languageList = null;
			System.gc();
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Tri des données");
			try { new FastQuickSort(new LanguageNameComparator()).sort(languageArray); } catch (Exception e) { Log.warn(CAT, e); }
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Sauvegarde des villes");
//			Log.debug("About to save cities");
			languagePersistenceService.saveLanguageArray(languageArray);
	   		progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Chargement terminé");
//			Log.debug("Cities saved");
			languageArray = null;
			System.gc();
	
			// Notify end of progress 
//			progressDispatcher.fireEvent(eventType, eventMessage, eventData);
		}
		finally {
			Log.debug("Disposing languagePersistenceService");
			languagePersistenceService.dispose();
		}
	}

}
