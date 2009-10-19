package org.helyx.app.j2me.getabike.data.language.service;


import java.util.Vector;

import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.data.language.serializer.LanguageSerializer;
import org.helyx.helyx4me.filter.record.RecordFilter;
import org.helyx.helyx4me.rms.IMultiRecordDao;
import org.helyx.helyx4me.rms.MultiRecordDao;
import org.helyx.helyx4me.rms.MultiRecordEnumeration;
import org.helyx.logging4me.Logger;


public class LanguagePersistenceService implements ILanguagePersistenceService {
	
	private static final Logger logger = Logger.getLogger("LANGUAGE_PERSISTENCE_SERVICE");
	
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

	public Vector findAllLanguages() {
		return getLanguageDao().findAllRecords();
	}

	public Language findLanguageByKey(String languageKey) {
		return null;
//		Language language = getLanguageDao().findLanguageByKey(languageKey);
//
//		return language;
	}

	public void removeAllLanguages() {
		getLanguageDao().removeAllRecords();
	}

	public void saveLanguages(Vector languageList) {
		
		int languageListSize = languageList.size();
		Language[] languageArray = new Language[languageListSize];
		languageList.copyInto(languageArray);

		getLanguageDao().saveRecordArray(languageArray);
	}

	public MultiRecordEnumeration createLanguageEnumeration(RecordFilter recordFilter) {
		return getLanguageDao().createRecordEnumeration(recordFilter);
	}

	public void destroyLanguageEnumeration(MultiRecordEnumeration languageEnumeration) {
		getLanguageDao().destroyRecordEnumeration(languageEnumeration);
	}

	public int countLanguages() {
		return getLanguageDao().countRecords();
	}

}


