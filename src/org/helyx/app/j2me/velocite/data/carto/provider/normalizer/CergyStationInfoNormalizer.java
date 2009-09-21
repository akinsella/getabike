package org.helyx.app.j2me.velocite.data.carto.provider.normalizer;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class CergyStationInfoNormalizer extends VelibStationInfoNormalizer {

	public CergyStationInfoNormalizer() {
		super();
	}

	public CergyStationInfoNormalizer(char splitter) {
		super(splitter);
	}

	public void normalizeName(Station station) {
		super.normalizeName(station);
		
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
				
		station.name = nameToNormalize.substring(index + 1).trim();
		
	}

}
