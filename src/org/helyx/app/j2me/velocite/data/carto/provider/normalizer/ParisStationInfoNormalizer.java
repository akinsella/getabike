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
		
		int stationAddressLength = station.address.length();
		int stationFullAddressLength = station.fullAddress.length();
		
		if (station.fullAddress.startsWith(station.address) && stationFullAddressLength - stationAddressLength > 5) {
			int index = stationAddressLength;
			if (index >= 0) {
				station.zipCode = station.fullAddress.substring(index + 1, index + 1 + 5);
				station.city = station.fullAddress.substring(index + 1 + 5 + 1);
			}
		}

		String separator = "-";
		
		int separatorIndex = station.address.indexOf(separator);
		if (separatorIndex > 0) {
			station.address = station.address.substring(0, separatorIndex).trim();
		}
		
		station.fullAddress = station.address;

	}

}
