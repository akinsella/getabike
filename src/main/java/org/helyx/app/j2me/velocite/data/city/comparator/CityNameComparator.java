package org.helyx.app.j2me.velocite.data.city.comparator;

import org.helyx.app.j2me.lib.comparator.Comparator;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.velocite.data.city.domain.City;


public class CityNameComparator implements Comparator {

	private static final Logger logger = LoggerFactory.getLogger("CITY_NAME_COMPARATOR");
	
	public int compare(Object object1, Object object2) {
		if (object1 == null && object2 == null) {
			return 0;
		}
		if (object1 == null) {
			return -1;
		}
		if (object2 == null) {
			return 1;
		}
		
		int result = ((City)object1).name.compareTo(((City)object2).name);
		
		return result;
	}

}
