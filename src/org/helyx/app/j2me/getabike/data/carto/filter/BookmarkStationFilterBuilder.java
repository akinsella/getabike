package org.helyx.app.j2me.getabike.data.carto.filter;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.filter.Filter;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.logging4me.Logger;

public class BookmarkStationFilterBuilder implements FilterBuilder {
	
	private Logger logger = Logger.getLogger(BookmarkStationFilterBuilder.class);
	
	private City city;

	public BookmarkStationFilterBuilder(City city) {
		super();
		this.city = city;
	}

	public Filter buildFilter() {
		int[] stationNumbers = CartoManager.getBookmarkedStationNumbers(city);
		
		return new StationNumbersFilter(stationNumbers);
	}

}
