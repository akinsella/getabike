package org.helyx.app.j2me.getabike.data.carto.provider.normalizer;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;

public class VelibStationInfoNormalizer implements IStationInfoNormalizer {

	protected static final char DEFAULT_SPLITTER = '-';
	protected static final String TPE_PATTERN = "(CB)";
	protected char splitter = DEFAULT_SPLITTER;

	public VelibStationInfoNormalizer() {
		this(DEFAULT_SPLITTER);
	}

	public VelibStationInfoNormalizer(char splitter) {
		super();
		this.splitter = splitter;
	}


	public void normalizeName(Station station) {
		
		if (station == null) {
			return ;
		}
		
		String nameToNormalize = station.name;
	
		int firstSplitterIndex = nameToNormalize.indexOf(splitter);
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

		
		int separatorIndex = station.address.lastIndexOf(splitter);
		if (separatorIndex > 0) {
			station.address = station.address.substring(0, separatorIndex).trim();
		}
		
		station.fullAddress = station.address;
		
		int tpeIndex = station.name.indexOf(TPE_PATTERN);
		if (tpeIndex >= 0) {
			station.tpe = true;
			station.name = station.name.substring(0, tpeIndex).trim();
		}

	}

}
