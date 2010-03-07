package org.helyx.app.j2me.getabike.data.language.service;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;

public interface ILanguagePersistenceService {

	Language findLanguageByKey(String languageKey);
	
	void saveLanguages(Vector languageList);
	
	Vector findAllLanguages();

	void removeAllLanguages();

	MultiRecordEnumeration createLanguageEnumeration(RecordFilter recordFilter);
	
	void destroyLanguageEnumeration(MultiRecordEnumeration languageEnumeration);

	int countLanguages();
	
	void dispose();

}


