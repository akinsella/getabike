package org.helyx.app.j2me.getabike.data.language.manager;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.data.language.provider.DefaultLanguageContentProvider;
import org.helyx.app.j2me.getabike.data.language.service.LanguagePersistenceService;
import org.helyx.app.j2me.getabike.ui.view.LanguageListView;
import org.helyx.app.j2me.getabike.lib.cache.Cache;
import org.helyx.app.j2me.getabike.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.logging4me.Logger;

public class LanguageManager {

	private static final Logger logger = Logger.getLogger("LANGUAGE_MANAGER");
	
	private static final String LANGUAGE_LIST = "language.list";
	
	private static Cache cache = new Cache();
	
	private LanguageManager() {
		super();
	}

	public static IProgressTask createUpdateLanguagesTask() {
		
		IContentAccessor languageContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/getabike/data/language/languages.xml");
		IContentProvider contentProvider = new DefaultLanguageContentProvider(languageContentAccessor);
		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(contentProvider);

		return progressTask;
	}

	public static Language getCurrentLanguage() {
		Vector languageList = findAllLanguages();
		Language selectedLanguage = null;

		String currentLanguageKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_CURRENT_KEY);

		if (logger.isInfoEnabled()) {
			logger.info("Current Language key: " + currentLanguageKey);
		}
		
		if (currentLanguageKey != null) {
			Enumeration _enum = languageList.elements();
			while(_enum.hasMoreElements()) {
				Language language = (Language)_enum.nextElement();
				if (language.key.equals(currentLanguageKey)) {
					selectedLanguage = language;
					break;
				}
			}
		}
		
		if (selectedLanguage == null && languageList.size() > 0) {
			selectedLanguage = (Language)languageList.elementAt(0);
			PrefManager.writePref(PrefConstants.LANGUAGE_CURRENT_KEY, selectedLanguage.key);
		}
		
		if (selectedLanguage == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("No Current language");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Current language: " + selectedLanguage);
		}
		
		return selectedLanguage;
	}
	
	public static void saveLanguages(Vector languageList) {
		cache.set(LANGUAGE_LIST, languageList);
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();

		try {
			languagePersistenceService.saveLanguages(languageList);
		}
		finally {
			languagePersistenceService.dispose();
		}
	}

	public static Vector findAllLanguages() {
		Vector languageList = (Vector)cache.get(LANGUAGE_LIST);
		if (languageList != null) {
			return languageList;
		}
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			languageList = languagePersistenceService.findAllLanguages();
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
			int count = languagePersistenceService.countLanguages();
			
			return count;
		}
		finally {
			languagePersistenceService.dispose();
		}
	}

	public static void clearCurrentLanguage() {
		PrefManager.removePref(PrefConstants.LANGUAGE_CURRENT_KEY);
	}

	public static void setCurrentLanguage(Language language) {
		setCurrentLanguage(language.key);
	}

	public static void setCurrentLanguage(String languageKey) {
		PrefManager.writePref(PrefConstants.LANGUAGE_CURRENT_KEY, languageKey);
	}
	
	public static void cleanUpSavedData() {
		cache.remove(LANGUAGE_LIST);
		clearCurrentLanguage();
		LanguagePersistenceService languagePersistenceService = new LanguagePersistenceService();
		try {
			languagePersistenceService.removeAllLanguages();
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
			DialogUtil.showAlertMessage(currentDisplayable, "dialog.title.error", currentDisplayable.getMessage("manager.language.error", re.getMessage()));
		}
	}

	public static void configureLocaleWithCurentLanguage(AbstractMIDlet midlet) {
		Language selectedLanguage = LanguageManager.getCurrentLanguage();
		midlet.setLocale(new Locale(selectedLanguage.localeCountry, selectedLanguage.localeLanguage));
		midlet.loadCurrentLocale();
	}
	
}
