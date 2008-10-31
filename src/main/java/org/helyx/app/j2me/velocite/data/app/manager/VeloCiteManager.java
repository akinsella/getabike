package org.helyx.app.j2me.velocite.data.app.manager;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;

public class VeloCiteManager {
	
	private static final Log log = LogFactory.getLog("VELOCITE_MANAGER");
	
	public static void cleanUpData() {
		cleanUpCitySelectedData();
		CityManager.cleanUpData();
	}
	
	public static void cleanUpCitySelectedData() {
		log.info("cleanUpCitySelectedData");
		CityManager.removeSelectedCity();
		CartoManager.cleanUpData();
	}
	
}
