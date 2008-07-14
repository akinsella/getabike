package org.helyx.app.j2me.velocite.data.carto.util;

import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class LocalizationUtil {
	
	private static final String CAT = "LOCALIZATION_UTIL";
	
	private LocalizationUtil() {
		super();
	}

	public static boolean isSet(Point localization) {
		if (localization == null) {
			return true;
		}
		
		return localization.lat == 0 || localization.lng == 0;
	}

}
