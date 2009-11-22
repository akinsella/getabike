package org.helyx.app.j2me.getabike.util;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

import org.helyx.helyx4me.localization.Point;
import org.helyx.logging4me.Logger;

public class LocationManager {
	
	private static final Logger logger = Logger.getLogger(LocationManager.class);

	private LocationManager() {
		super();
	}
	
	public static Point getLocations(boolean opeModeEnabled) {
		try {
			// Criteria can be used to filter which GPS device to use.
			Criteria criteria = new Criteria();
			criteria.setCostAllowed(opeModeEnabled);
			criteria.setPreferredPowerConsumption(Criteria.NO_REQUIREMENT);
	
			// Get a location provider based on the aforementioned criteria.
			LocationProvider provider = LocationProvider.getInstance(criteria);
	
			// Try to fetch the current location (using a 3 minute timeout).
			Location internalLocation = provider.getLocation(60);
	
			// Get the coordinates of the current location.
			javax.microedition.location.Coordinates coordinates = internalLocation.getQualifiedCoordinates();
			
			return new Point(coordinates.getLongitude(), coordinates.getLatitude());
		} 
		catch (Throwable t) {
			logger.warn(t);
			return null;
		}

	}

	
}
