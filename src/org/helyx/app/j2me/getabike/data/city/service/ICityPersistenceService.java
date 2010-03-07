package org.helyx.app.j2me.getabike.data.city.service;

import java.util.Vector;

import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;

public interface ICityPersistenceService {

	City findCityByKey(String cityKey);
	
	void saveCityArray(City[] cityArray);
	
	Vector findAllCities();
	
	Vector findAllCountries();
	
	Vector findAllCitiesByCountryName(String countryName);

	void removeAllCities();

	MultiRecordEnumeration createCityEnumeration(RecordFilter recordFilter);
	
	void destroyCityEnumeration(MultiRecordEnumeration cityEnumeration);

	int countCities();

	int countCitiesByCountryName(String countryName);
	
	void dispose();

}


