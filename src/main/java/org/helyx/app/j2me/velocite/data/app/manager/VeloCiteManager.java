package org.helyx.app.j2me.velocite.data.app.manager;

import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;

public class VeloCiteManager {
	
	private static final Logger logger = LoggerFactory.getLogger("VELOCITE_MANAGER");
	
	public static void cleanUpData() {
		cleanUpCitySelectedData();
		CityManager.cleanUpData();
	}
	
	public static void cleanUpCitySelectedData() {
		logger.info("cleanUpCitySelectedData");
		CityManager.removeSelectedCity();
		CartoManager.cleanUpData();
	}
	
}
