package org.helyx.app.j2me.velocite.task;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.listener.LanguageLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;

public class LanguageConfigurationTask extends AbstractProgressTask {
	
	private static final Log log = LogFactory.getLog("LANGUAGE_CONFIGURATION_TASK");
	
	private Canvas canvas;
	private AbstractMIDlet midlet;
	
	public LanguageConfigurationTask(AbstractMIDlet midlet, Canvas canvas) {
		super("Détection des touches");
		this.midlet = midlet;
		this.canvas = canvas;
	}
	
	public void execute() {
		try {
			progressDispatcher.fireEvent(ProgressEventType.ON_START);
	
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Recherche de config. existante...");
			String languageSelectedKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_SELECTED_KEY);
			
			if (languageSelectedKey == null) {
				log.info("Chargement des langues ...");
				IProgressTask progressTask = LanguageManager.refreshDataWithDefaults();
				progressTask.addProgressListener(new LanguageLoaderProgressListener(progressTask.getProgressDispatcher()));
				progressTask.addProgressListener(new ProgressAdapter() {

					public void onProgress(String eventMessage, Object eventData) {
						progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, eventMessage, eventData);
					}

					public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
						progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, eventMessage, eventData);
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

			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS);
		}
		catch(Throwable t) {
			log.warn(t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t.getMessage(), t);
		}
		
	}
	
}
