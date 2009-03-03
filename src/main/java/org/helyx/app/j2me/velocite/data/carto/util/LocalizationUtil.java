package org.helyx.app.j2me.velocite.data.carto.util;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class LocalizationUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("LOCALIZATION_UTIL");
	
	private LocalizationUtil() {
		super();
	}

	public static boolean isSet(Point localization) {
		if (localization == null) {
			return false;
		}
		
		return localization.lat != 0 && localization.lng != 0;
	}

}
