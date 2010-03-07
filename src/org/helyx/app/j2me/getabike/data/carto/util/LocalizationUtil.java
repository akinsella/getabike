package org.helyx.app.j2me.getabike.data.carto.util;

import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.logging4me.Logger;


public class LocalizationUtil {
	
	private static final Logger logger = Logger.getLogger("LOCALIZATION_UTIL");
	
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
