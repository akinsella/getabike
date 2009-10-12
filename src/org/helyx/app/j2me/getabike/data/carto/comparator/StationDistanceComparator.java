package org.helyx.app.j2me.getabike.data.carto.comparator;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.helyx4me.comparator.Comparator;


public class StationDistanceComparator implements Comparator {

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

		Station station1 = (Station)object1;
		Station station2 = (Station)object2;
		
		return station1.distance < station2.distance ? -1 : (station1.distance > station2.distance ? 1 : 0);
	}

}
