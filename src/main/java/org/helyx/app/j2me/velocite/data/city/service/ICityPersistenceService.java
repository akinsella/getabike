package org.helyx.app.j2me.velocite.data.city.service;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public interface ICityPersistenceService {

	City findCityByKey(String cityKey);
	
	void saveCityArray(City[] cityArray);
	
	Vector findAllCities();

	void removeAllCities();

	MultiRecordEnumeration createCityEnumeration(IRecordFilter recordFilter);
	
	void destroyCityEnumeration(MultiRecordEnumeration cityEnumeration);

	int countCities();
	
	void dispose();

}


