package org.helyx.app.j2me.getabike.data.carto.provider.normalizer;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;

public class SimpleStationInfoNormalizer implements IStationInfoNormalizer {

	protected static final char DEFAULT_SPLITTER = '-';
	protected static final String TPE_PATTERN = "(CB)";

	private char splitter = DEFAULT_SPLITTER;
	
	public SimpleStationInfoNormalizer() {
		this(DEFAULT_SPLITTER);
	}
	
	public SimpleStationInfoNormalizer(char splitter) {
		this.splitter = splitter;
	}

	public void normalizeName(Station station) {
		
		if (station == null) {
			return ;
		}
		
		String nameToNormalize = station.name;
		
		// No more check to do
		if (nameToNormalize == null) {
			return ;
		}
		
		int index = nameToNormalize.indexOf(splitter);
		
		// No more check to do
		if (index < 0) {
			return ;
		}
		
		int stationNumberIndex = nameToNormalize.indexOf(String.valueOf(station.number));
		
		if (stationNumberIndex >= 0 && stationNumberIndex < index) {
			station.name = nameToNormalize.substring(index + 1).trim();
		}
		
		int tpeIndex = station.name.indexOf(TPE_PATTERN);
		if (tpeIndex >= 0) {
			station.tpe = true;
			station.name = station.name.substring(0, tpeIndex).trim();
		}
	
		return ;
	}

}
