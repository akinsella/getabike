package org.helyx.app.j2me.lib.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.map.google.GoogleMaps;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class GoogleMapTest extends TestCase {
	
	private static final Logger logger = LoggerFactory.getLogger("GOOGLE_MAP_TEST");

	public GoogleMapTest() {
		super();
	}
	
	public void testGoogleMapLocalizationScrollLeft() {
		GoogleMaps googleMaps = new GoogleMaps("ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA");
		Point localization = new Point(48.8610789316, 2.40036207675);
		logger.info("Localization: " + localization);
		Point newLocalization = googleMaps.adjust(localization, 0, 0, 14);
		logger.info("New X localization: " + newLocalization);

		Point newLocalization2 = googleMaps.adjust(localization, 0, 0, 19);
		logger.info("New Y localization: " + newLocalization2);
	}
	
}
