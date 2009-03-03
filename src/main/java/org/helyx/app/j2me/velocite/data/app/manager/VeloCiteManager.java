package org.helyx.app.j2me.velocite.data.app.manager;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;

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
