package org.helyx.app.j2me.velocite.data.carto.provider.normalizer;

import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class ParisStationInfoNormalizer implements IStationInfoNormalizer {

	private static final String SPLITTER = "-";
	
	public ParisStationInfoNormalizer() {
		super();
	}

	public void normalizeName(Station station) {
		
		if (station == null) {
			return ;
		}
		
		String nameToNormalize = station.name;
	
		int firstSplitterIndex = nameToNormalize != null ? nameToNormalize.indexOf(SPLITTER) : -1;
		if (firstSplitterIndex >= 0) {
			int stationNumberIndex = nameToNormalize.indexOf(String.valueOf(station.number));
			
			if (stationNumberIndex >= 0 && stationNumberIndex < firstSplitterIndex) {
				station.name = nameToNormalize.substring(firstSplitterIndex + 1).trim();
			}
		}


		String zipCodeAndCityInfo = " - " + station.zipCode + " "  + station.city;
		
		int zipCodeAndCityInfoIndex = station.fullAddress != null ? station.fullAddress.indexOf(zipCodeAndCityInfo) : -1;
		if (zipCodeAndCityInfoIndex > 0) {
			station.fullAddress = station.fullAddress.substring(0, zipCodeAndCityInfoIndex);
		}

		String separator = " -";
		
		int separatorIndex = station.address != null ? station.address.indexOf(separator) : -1;
		if (separatorIndex > 0) {
			station.address = station.address.substring(0, separatorIndex);
		}

	}

}
