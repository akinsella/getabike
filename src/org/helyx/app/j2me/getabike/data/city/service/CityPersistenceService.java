package org.helyx.app.j2me.getabike.data.city.service;


import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.serializer.CitySerializer;
import org.helyx.helyx4me.filter.record.RecordFilter;
import org.helyx.helyx4me.rms.IMultiRecordDao;
import org.helyx.helyx4me.rms.MultiRecordDao;
import org.helyx.helyx4me.rms.MultiRecordEnumeration;
import org.helyx.logging4me.Logger;


public class CityPersistenceService implements ICityPersistenceService {
	
	private static final Logger logger = Logger.getLogger("CITY_PERSISTENCE_SERVICE");
	
	private static final String CITY_RECORD_STORE_NAME = "city";
	
	private IMultiRecordDao cityDao;
	
	public CityPersistenceService() {
		super();
	}
	
	public void dispose() {
		if (cityDao != null) {
			cityDao.dispose();
		}
	}
	
	private IMultiRecordDao getCityDao() {
		if (cityDao == null) {
			cityDao = new MultiRecordDao(CITY_RECORD_STORE_NAME, new CitySerializer(), 1024);
		}
		
		return cityDao;
	}

	public Vector findAllCities() {
		return getCityDao().findAllRecords();
	}

	public Vector findAllCitiesByCountryName(String countryName) {
		Vector cityList = new Vector();
		Vector tmpCityList = getCityDao().findAllRecords();
		Enumeration cityEnum = tmpCityList.elements();
		while(cityEnum.hasMoreElements()) {
			City city = (City)cityEnum.nextElement();
			if (city.country.equals(countryName)) {
				cityList.addElement(city);
			}
		}
		
		return cityList;
	}

	public Vector findAllCountries() {
		Vector countryList = new Vector();
		Vector tmpCityList = getCityDao().findAllRecords();
		Enumeration cityEnum = tmpCityList.elements();
		while(cityEnum.hasMoreElements()) {
			City city = (City)cityEnum.nextElement();
			if (!countryList.contains(city.country)) {
				countryList.addElement(city.country);
			}
		}
		
		return countryList;
	}

	public City findCityByKey(String cityKey) {
		Vector tmpCityList = getCityDao().findAllRecords();
		Enumeration cityEnum = tmpCityList.elements();
		while(cityEnum.hasMoreElements()) {
			City city = (City)cityEnum.nextElement();
			if (city.key.equals(cityKey)) {
				return city;
			}
		}
		
		return null;
	}

	public void removeAllCities() {
		getCityDao().removeAllRecords();
	}

	public void saveCityArray(City[] cityArray) {
		if (logger.isDebugEnabled()) {
			logger.debug("Saving cities ...");
		}

		getCityDao().saveRecordArray(cityArray);

		if (logger.isDebugEnabled()) {
			logger.debug("Cities saved ...");
		}
	}

	public MultiRecordEnumeration createCityEnumeration(RecordFilter recordFilter) {
		return getCityDao().createRecordEnumeration(recordFilter);
	}

	public void destroyCityEnumeration(MultiRecordEnumeration cityEnumeration) {
		getCityDao().destroyRecordEnumeration(cityEnumeration);
	}

	public int countCities() {
		return getCityDao().countRecords();
	}

	public int countCitiesByCountryName(String countryName) {
		Vector cityList = new Vector();
		Vector tmpCityList = getCityDao().findAllRecords();
		Enumeration cityEnum = tmpCityList.elements();
		while(cityEnum.hasMoreElements()) {
			City city = (City)cityEnum.nextElement();
			if (city.country.equals(countryName)) {
				cityList.addElement(city);
			}
		}
		
		return cityList.size();
	}

}


