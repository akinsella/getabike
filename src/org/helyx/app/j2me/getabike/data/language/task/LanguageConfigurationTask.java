package org.helyx.app.j2me.velocite.data.language.task;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.listener.LanguageLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.helyx4me.concurrent.Future;
import org.helyx.helyx4me.i18n.Locale;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;


public class LanguageConfigurationTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("LANGUAGE_CONFIGURATION_TASK");
	
	private AbstractView view;
	
	public LanguageConfigurationTask(AbstractView view) {
		super(view.getMessage("task.language.title"));
		this.view = view;
	}
	
	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				try {
					progressDispatcher.fireEvent(EventType.ON_START);
			
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.language.search.message"));
					String languageSelectedKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_CURRENT_KEY);
					
					if (languageSelectedKey == null) {
						if (logger.isInfoEnabled()) {
							logger.info(view.getMessage("task.language.load.message"));
						}
						IProgressTask progressTask = LanguageManager.refreshDataWithDefaults();
						progressTask.addProgressListener(new LanguageLoaderProgressListener(progressTask.getProgressDispatcher(), view));
						progressTask.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {

							public void onProgress(String eventMessage, Object eventData) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage, eventData);
							}

							public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, eventMessage, eventData);
							}
							
						});
						Vector languageList = (Vector)Future.get(progressTask);
						String defaultLanguageKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_DEFAULT_KEY);
						Enumeration _enum = languageList.elements();
						while(_enum.hasMoreElements()) {
							Language language = (Language)_enum.nextElement();
							if (language.key.equals(defaultLanguageKey)) {
								PrefManager.writePref(PrefConstants.LANGUAGE_CURRENT_KEY, language.key);
							}
						}
					}
					Language selectedLanguage = LanguageManager.getCurrentLanguage();
					view.getMidlet().setLocale(new Locale(selectedLanguage.localeCountry, selectedLanguage.localeLanguage));

					progressDispatcher.fireEvent(EventType.ON_SUCCESS);
				}
				catch(Throwable t) {
					logger.warn(t);
					progressDispatcher.fireEvent(EventType.ON_ERROR, t.getMessage(), t);
				}				
			}
			
		};
	}

	
}
