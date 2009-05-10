package org.helyx.app.j2me.velocite.data.language.task;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.listener.LanguageLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.helyx4me.concurrent.Future;
import org.helyx.helyx4me.i18n.Locale;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.logging4me.Logger;


public class LanguageConfigurationTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("LANGUAGE_CONFIGURATION_TASK");
	
	private Canvas canvas;
	private AbstractMIDlet midlet;
	
	public LanguageConfigurationTask(AbstractMIDlet midlet, Canvas canvas) {
		super("Détection des touches");
		this.midlet = midlet;
		this.canvas = canvas;
	}
	
	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				try {
					progressDispatcher.fireEvent(EventType.ON_START);
			
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Recherche de config. existante...");
					String languageSelectedKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_SELECTED_KEY);
					
					if (languageSelectedKey == null) {
						logger.info("Chargement des langues ...");
						IProgressTask progressTask = LanguageManager.refreshDataWithDefaults();
						progressTask.addProgressListener(new LanguageLoaderProgressListener(progressTask.getProgressDispatcher()));
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
								PrefManager.writePref(PrefConstants.LANGUAGE_SELECTED_KEY, language.key);
							}
						}
					}
					Language selectedLanguage = LanguageManager.findSelectedLanguage();
					midlet.setLocale(new Locale(selectedLanguage.localeCountry, selectedLanguage.localeLanguage));

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
