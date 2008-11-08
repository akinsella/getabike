package org.helyx.app.j2me.lib.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.map.google.GoogleMaps;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class GoogleMapTest extends TestCase {
	
	private static final Log log = LogFactory.getLog("GOOGLE_MAP_TEST");

	public GoogleMapTest() {
		super();
	}
	
	public void testGoogleMapLocalizationScrollLeft() {
		GoogleMaps googleMaps = new GoogleMaps("ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA");
		Point localization = new Point(48.8610789316, 2.40036207675);
		log.info("Localization: " + localization);
		Point newLocalization = googleMaps.adjust(localization, 0, 0, 14);
		log.info("New X localization: " + newLocalization);

		Point newLocalization2 = googleMaps.adjust(localization, 0, 0, 19);
		log.info("New Y localization: " + newLocalization2);
	}
	
}
