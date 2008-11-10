package org.helyx.app.j2me.lib.map.google;

import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public interface POIInfoAccessor {

	String getName(Object object);
	
	String getDescription(Object object);
	
	Point getLocalization(Object object);
	
}
