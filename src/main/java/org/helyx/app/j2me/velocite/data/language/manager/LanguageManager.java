package org.helyx.app.j2me.velocite.data.language.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.provider.DefaultLanguageContentProvider;
import org.helyx.app.j2me.velocite.data.language.service.LanguagePersistenceService;
import org.helyx.app.j2me.velocite.ui.view.LanguageListView;
import org.helyx.helyx4me.content.accessor.ClasspathContentAccessor;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;

public class LanguageManager {

	private static final Logger logger = LoggerFactory.getLogger("LANGUAGE_MANAGER");
	
	private LanguageManager() {
		super();
	}

	public static IProgressTask refreshDataWithDefaults() {
		
		IContentAccessor languageContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/language/languages.xml");
		IContentProvider contentProvider = new DefaultLanguageContentProvider(languageContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

	public static Language findSelectedLanguage() throws LanguageManagerException {
		Vector languageList = findAllLanguages();
		Language selectedLanguage = findSelectedLanguage(languageList);
		
		return selectedLanguage;
	}
	
	public static Language findSelectedLanguage(Vector languageList) throws LanguageManagerException {
		Language selectedLanguage = null;

		String languageSelectedKeyPrefValue = PrefManager.readPrefString(PrefConstants.LANGUAGE_SELECTED_KEY);
		logger.info("Selected Language key: " + languageSelectedKeyPrefValue);
		String languageDefaultKeyPrefValue = PrefManager.readPrefString(PrefConstants.LANGUAGE_DEFAULT_KEY);
		logger.info("Default Language key: " + languageSelectedKeyPrefValue);
		
		Enumeration _enum = languageList.elements();
		while(_enum.hasMoreElements()) {
			Language language = (Language)_enum.nextElement();
			if (language.key.equals(languageSelectedKeyPrefValue)) {
				selectedLanguage = language;
				break;
			}
		}

		if (selectedLanguage == null) {
			_enum = languageList.elements();
			while(_enum.hasMoreElements()) {
				Language language = (Language)_enum.nextElement();
				if (language.key.equals(languageDefaultKeyPrefValue)) {
					selectedLanguage = language;
					PrefManager.writePref(PrefConstants.LANGUAGE_SELECTED_KEY, selectedLanguage.key);
					break;
				}
			}
		}
		
		if (selectedLanguage == null) {
			throw new LanguageManagerException("No default/active language exception");
		}

		logger.debug("Selected language: " + selectedLanguage);
		
		return selectedLanguage;
	}

	public static Vector findAllLanguages() {
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			Vector languageList = languagePersistenceService.findAllCities();
			
			return languageList;
		}
		finally {
			languagePersistenceService.dispose();
		}
	}

	public static int countLanguages() {
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			int count = languagePersistenceService.countCities();
			
			return count;
		}
		finally {
			languagePersistenceService.dispose();
		}
	}

	public static void saveSelectedLanguage(Language language) {
		PrefManager.writePref(PrefConstants.LANGUAGE_SELECTED_KEY, language.key);
	}
	
	public static void cleanUpSavedData() {
		PrefManager.removePref(PrefConstants.LANGUAGE_DEFAULT_KEY);
		PrefManager.removePref(PrefConstants.LANGUAGE_SELECTED_KEY);
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			languagePersistenceService.removeAllCities();
		}
		finally {
			languagePersistenceService.dispose();
		}
	}
	
	public static void showLanguageView(AbstractDisplayable currentDisplayable) {
		LanguageListView languageListView;
		try {
			languageListView = new LanguageListView(currentDisplayable.getMidlet());
			languageListView.setPreviousDisplayable(currentDisplayable);
			currentDisplayable.showDisplayable(languageListView);
		}
		catch (RuntimeException e) {
			logger.warn(e);
			currentDisplayable.showAlertMessage("Problème de configuration", "Le fichier des langues n'est pas valide: " + e.getMessage());
		}
	}
	
}
