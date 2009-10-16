package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.helyx4me.filter.ChainedFilter;
import org.helyx.helyx4me.filter.Filter;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.helyx4me.pref.PrefManager;

public class DefaultStationFilterBuilder implements FilterBuilder {
	
	public DefaultStationFilterBuilder() {
		super();
	}

	public Filter buildFilter() {
		ChainedFilter chainedFilter = new ChainedFilter();
		
		String stationNameFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_NAME_FILTER);
		if (stationNameFilter != null && stationNameFilter.length() > 0) {
			chainedFilter.addFilter(new StationNameFilter(stationNameFilter));
		}

		String zipCodeFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_ZIPCODE_FILTER);
		if (zipCodeFilter != null && zipCodeFilter.length() > 0) {
			chainedFilter.addFilter(new StationZipCodeFilter(zipCodeFilter));
		}

		String cityFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_CITY_FILTER);
		if (cityFilter != null && cityFilter.length() > 0) {
			chainedFilter.addFilter(new StationCityFilter(cityFilter));
		}

		return chainedFilter;
	}

}
