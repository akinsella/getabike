package org.helyx.app.j2me.velocite.data.language.listener;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.sort.FastQuickSort;
import org.helyx.app.j2me.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.velocite.data.language.LanguageConstants;
import org.helyx.app.j2me.velocite.data.language.comparator.LanguageNameComparator;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.service.ILanguagePersistenceService;
import org.helyx.app.j2me.velocite.data.language.service.LanguagePersistenceService;

public class LanguageLoaderProgressListener extends ProgressAdapter {

	private static final Log log = LogFactory.getLog("LANGUAGE_LOADER_PROGRESS_LISTENER");
	
	private ILanguagePersistenceService languagePersistenceService;
	
	private Vector languageList;
	private Language[] languageArray;
	private IProgressDispatcher progressDispatcher;

	public LanguageLoaderProgressListener(IProgressDispatcher progressDispatcher) {
		super();
		this.progressDispatcher = progressDispatcher;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.languagePersistenceService = new LanguagePersistenceService();
		this.languageList = new Vector();

//		progressDispatcher.fireEvent(ProgressEventType.ON_START);
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Suppression des langues");
		languagePersistenceService.removeAllCities();
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == LanguageConstants.ON_LANGUAGE_LOADED) {
			Language language = (Language)eventData;
			languageList.addElement(language);
			int size = languageList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, languageList.size() + " langues charg�es");
			}
		}
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		try {
			int languageListSize = languageList.size();
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, languageListSize + " langues charg�es");
			languageArray = new Language[languageListSize];
			languageList.copyInto(languageArray);

//			languageList = null;
			System.gc();
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Tri des donn�es");
			try { new FastQuickSort(new LanguageNameComparator()).sort(languageArray); } catch (Exception e) { log.warn(e); }
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Sauvegarde des villes");
//			log.debug("About to save cities");
			languagePersistenceService.saveLanguageArray(languageArray);
	   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Chargement termin�");
//			log.debug("Cities saved");
			languageArray = null;
			System.gc();
	
			// Notify end of progress 
//			progressDispatcher.fireEvent(eventType, eventMessage, eventData);
		}
		finally {
			log.debug("Disposing languagePersistenceService");
			languagePersistenceService.dispose();
		}
	}

}
