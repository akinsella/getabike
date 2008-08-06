package org.helyx.app.j2me.velocite.data.language.service;


import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.rms.IMultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.serializer.LanguageSerializer;

public class LanguagePersistenceService implements ILanguagePersistenceService {
	
	private static final String CAT = "LANGUAGE_PERSISTENCE_SERVICE";
	
	private static final String LANGUAGE_RECORD_STORE_NAME = "language";
	
	private IMultiRecordDao languageDao;
	
	public LanguagePersistenceService() {
		super();
	}
	
	public void dispose() {
		if (languageDao != null) {
			languageDao.dispose();
		}
	}
	
	private IMultiRecordDao getLanguageDao() {
		if (languageDao == null) {
			languageDao = new MultiRecordDao(LANGUAGE_RECORD_STORE_NAME, new LanguageSerializer(), 1024);
		}
		
		return languageDao;
	}

	public Vector findAllCities() {
		return getLanguageDao().findAllRecords();
	}

	public Language findLanguageByKey(String languageKey) {
		return null;
//		Language language = getLanguageDao().findLanguageByKey(languageKey);
//
//		return language;
	}

	public void removeAllCities() {
		getLanguageDao().removeAllRecords();
	}

	public void saveLanguageArray(Language[] languageArray) {
		getLanguageDao().saveRecordArray(languageArray);
	}

	public MultiRecordEnumeration createLanguageEnumeration(IRecordFilter recordFilter) {
		return getLanguageDao().createRecordEnumeration(recordFilter);
	}

	public void destroyLanguageEnumeration(MultiRecordEnumeration languageEnumeration) {
		getLanguageDao().destroyRecordEnumeration(languageEnumeration);
	}

	public int countCities() {
		return getLanguageDao().countRecords();
	}

}


