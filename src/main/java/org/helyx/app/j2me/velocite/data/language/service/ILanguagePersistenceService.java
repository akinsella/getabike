package org.helyx.app.j2me.velocite.data.language.service;

import java.util.Vector;

import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.helyx4me.filter.IRecordFilter;
import org.helyx.helyx4me.rms.MultiRecordEnumeration;

public interface ILanguagePersistenceService {

	Language findLanguageByKey(String languageKey);
	
	void saveLanguageArray(Language[] languageArray);
	
	Vector findAllCities();

	void removeAllCities();

	MultiRecordEnumeration createLanguageEnumeration(IRecordFilter recordFilter);
	
	void destroyLanguageEnumeration(MultiRecordEnumeration languageEnumeration);

	int countCities();
	
	void dispose();

}


