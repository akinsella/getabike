package org.helyx.app.j2me.getabike.data.language.listener;

import java.util.Vector;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.language.LanguageConstants;
import org.helyx.app.j2me.getabike.data.language.comparator.LanguageNameComparator;
import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.data.language.manager.LanguageManager;
import org.helyx.app.j2me.getabike.data.language.service.ILanguagePersistenceService;
import org.helyx.app.j2me.getabike.data.language.service.LanguagePersistenceService;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.sort.FastQuickSort;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressDispatcher;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;


public class LanguageLoaderProgressListener extends ProgressAdapter {

	private static final Logger logger = Logger.getLogger("LANGUAGE_LOADER_PROGRESS_LISTENER");
	
	private ILanguagePersistenceService languagePersistenceService;
	
	private Vector languageList;
	private Language[] languageArray;
	private IProgressDispatcher progressDispatcher;
	private AbstractView view;

	public LanguageLoaderProgressListener(IProgressDispatcher progressDispatcher, AbstractView view) {
		super(logger.getCategory().getName());
		this.progressDispatcher = progressDispatcher;
		this.view = view;
	}

	public void onStart(String eventMessage, Object eventData) {
		this.languagePersistenceService = new LanguagePersistenceService();
		this.languageList = new Vector();

//		progressDispatcher.fireEvent(ProgressEventType.ON_START);
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("progress.listener.loader.language.data.remove"));
		languagePersistenceService.removeAllLanguages();
	}
	
	public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
		if (eventType == LanguageConstants.ON_LANGUAGE_LOADED) {
			Language language = (Language)eventData;
			languageList.addElement(language);
			int size = languageList.size();
			
			if (size % 5 == 0) {
		   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, languageList.size() + " " + view.getMessage("progress.listener.loader.language.data.loaded"));
			}
		}
	}

	public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
		int languageListSize = languageList.size();
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, languageListSize + " " + view.getMessage("progress.listener.loader.language.data.loaded"));

   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("progress.listener.loader.language.data.sort"));
		try { new FastQuickSort(new LanguageNameComparator()).sort(languageArray); } catch (Exception e) { logger.warn(e); }
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("progress.listener.loader.language.data.save"));
//		logger.debug("About to save cities");
		LanguageManager.saveLanguages(languageList);
   		progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("progress.listener.loader.language.end"));
//		logger.debug("Cities saved");
		languageArray = null;
		System.gc();

		// Notify end of progress 
//		progressDispatcher.fireEvent(eventType, eventMessage, eventData);
	}

}
