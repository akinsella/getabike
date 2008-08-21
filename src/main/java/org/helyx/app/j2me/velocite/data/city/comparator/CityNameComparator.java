package org.helyx.app.j2me.velocite.data.city.comparator;

import org.helyx.app.j2me.lib.comparator.Comparator;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.city.domain.City;


public class CityNameComparator implements Comparator {

	private static final Log log = LogFactory.getLog("CITY_NAME_COMPARATOR");
	
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
