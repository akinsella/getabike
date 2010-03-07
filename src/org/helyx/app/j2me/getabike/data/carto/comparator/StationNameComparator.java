package org.helyx.app.j2me.getabike.data.carto.comparator;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.lib.comparator.Comparator;


public class StationNameComparator implements Comparator {

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

		return ((Station)object1).name.compareTo(((Station)object2).name);
	}

}
