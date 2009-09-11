package org.helyx.app.j2me.velocite.data.carto.provider.normalizer;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class SimpleCBStationInfoNormalizer implements IStationInfoNormalizer {

	private String splitter = "-";
	
	public SimpleCBStationInfoNormalizer(String splitter) {
		super();
		this.splitter = splitter;
	}

	public void normalizeName(Station station) {
		
		if (station == null) {
			return ;
		}
		
		String nameToNormalize = station.name;
			
		if (nameToNormalize == null) {
			return ;
		}
		
		int index = nameToNormalize.indexOf(splitter);
		if (index < 0) {
			return ;
		}
		int secondIndex = nameToNormalize.indexOf(String.valueOf(station.number));
		
		if (secondIndex >= 0 && secondIndex < index) {
			station.name = nameToNormalize.substring(index + 1).trim();
		}
		
		int tpeIndex = station.name.indexOf(" (CB)");
		if (tpeIndex >= 0) {
			station.tpe = true;
			station.name = station.name.substring(0, tpeIndex);
		}
		
		return ;
	}

}
