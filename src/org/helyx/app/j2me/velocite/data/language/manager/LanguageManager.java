package org.helyx.app.j2me.velocite.data.language.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.provider.DefaultLanguageContentProvider;
import org.helyx.app.j2me.velocite.data.language.service.LanguagePersistenceService;
import org.helyx.app.j2me.velocite.ui.view.LanguageListView;
import org.helyx.helyx4me.cache.Cache;
import org.helyx.helyx4me.content.accessor.ClasspathContentAccessor;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.logging4me.Logger;


public class LanguageManager {

	private static final Logger logger = Logger.getLogger("LANGUAGE_MANAGER");
	
	private static final String LANGUAGE_LIST = "language.list";
	
	private static Cache cache = new Cache();
	
	private LanguageManager() {
		super();
	}

	public static IProgressTask refreshDataWithDefaults() {
		
		IContentAccessor languageContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/language/languages.xml");
		IContentProvider contentProvider = new DefaultLanguageContentProvider(languageContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

	public static Language getCurrentLanguage() throws LanguageManagerException {
		Vector languageList = findAllLanguages();
		Language selectedLanguage = null;

		String languageSelectedKeyPrefValue = PrefManager.readPrefString(PrefConstants.LANGUAGE_CURRENT_KEY);
		if (logger.isInfoEnabled()) {
			logger.info("Current Language key: " + languageSelectedKeyPrefValue);
		}
		String languageDefaultKeyPrefValue = PrefManager.readPrefString(PrefConstants.LANGUAGE_DEFAULT_KEY);
		if (logger.isInfoEnabled()) {
			logger.info("Default Language key: " + languageSelectedKeyPrefValue);
		}
		
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
					PrefManager.writePref(PrefConstants.LANGUAGE_CURRENT_KEY, selectedLanguage.key);
					break;
				}
			}
		}
		
		if (selectedLanguage == null) {
			throw new LanguageManagerException("No default/active language exception");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Current language: " + selectedLanguage);
		}
		
		return selectedLanguage;
	}

	public static Vector findAllLanguages() {
		Vector languageList = (Vector)cache.get(LANGUAGE_LIST);
		if (languageList != null) {
			return languageList;
		}
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			languageList = languagePersistenceService.findAllCities();
			cache.set(LANGUAGE_LIST, languageList);
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
		PrefManager.writePref(PrefConstants.LANGUAGE_CURRENT_KEY, language.key);
	}
	
	public static void cleanUpSavedData() {
		cache.remove(LANGUAGE_LIST);
		PrefManager.removePref(PrefConstants.LANGUAGE_DEFAULT_KEY);
		PrefManager.removePref(PrefConstants.LANGUAGE_CURRENT_KEY);
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
		catch (RuntimeException re) {
			logger.warn(re);
			DialogUtil.showAlertMessage(currentDisplayable, currentDisplayable.getMessage("dialog.title.error"), currentDisplayable.getMessage("manage.language.error", re.getMessage()));
		}
	}
	
}
