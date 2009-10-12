package org.helyx.app.j2me.getabike.data.app.manager;

import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.logging4me.Logger;


public class GetABikeManager {
	
	private static final Logger logger = Logger.getLogger("GETABIKE_MANAGER");
	
	public static void cleanUpData() {
		cleanUpCitySelectedData();
		CityManager.clearCities();
	}
	
	public static void cleanUpCitySelectedData() {
		if (logger.isInfoEnabled()) {
			logger.info("cleanUpCitySelectedData");
		}
		CityManager.clearCurrentCountry();
		CityManager.clearCurrentCity(true);
	}
	
}
