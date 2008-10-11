package org.helyx.app.j2me.velocite.data.city.service;


import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.rms.DaoException;
import org.helyx.app.j2me.lib.rms.IMultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordDao;
import org.helyx.app.j2me.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.serializer.CitySerializer;

public class CityPersistenceService implements ICityPersistenceService {
	
	private static final Log log = LogFactory.getLog("CITY_PERSISTENCE_SERVICE");
	
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

	public City findCityByKey(String cityKey) {
		return null;
//		City city = getCityDao().findCityByKey(cityKey);
//
//		return city;
	}

	public void removeAllCities() {
		getCityDao().removeAllRecords();
	}

	public void saveCityArray(City[] cityArray) {
		log.debug("Saving cities ...");

		getCityDao().saveRecordArray(cityArray);

		log.debug("Cities saved ...");
	}

	public MultiRecordEnumeration createCityEnumeration(IRecordFilter recordFilter) {
		return getCityDao().createRecordEnumeration(recordFilter);
	}

	public void destroyCityEnumeration(MultiRecordEnumeration cityEnumeration) {
		getCityDao().destroyRecordEnumeration(cityEnumeration);
	}

	public int countCities() {
		return getCityDao().countRecords();
	}

}


