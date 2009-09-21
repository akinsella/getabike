package org.helyx.app.j2me.velocite.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.SimpleStationInfoNormalizer;
import org.helyx.logging4me.config.BasicConfigurer;

public class StationNameNormalizerTest extends TestCase {
	
	public StationNameNormalizerTest() {
		super();
		new BasicConfigurer().configure();
	}

	public void testSimpleStationNameNormalizer1() {
		IStationInfoNormalizer stationNmNameNormalizer = new SimpleStationInfoNormalizer();
		Station station = new Station();
		station.number = 12345;
		station.name = station.number + "-" + "bla bla";
		stationNmNameNormalizer.normalizeName(station);
		assertEquals("bla bla", station.name);
	}
	
	public void testSimpleStationNameNormalizer2() {
		IStationInfoNormalizer stationNmNameNormalizer = new SimpleStationInfoNormalizer();
		Station station = new Station();
		station.number = 12345;
		station.name = station.number + " - " + "bla bla";
		stationNmNameNormalizer.normalizeName(station);
		assertEquals("bla bla", station.name);
	}
	
}
