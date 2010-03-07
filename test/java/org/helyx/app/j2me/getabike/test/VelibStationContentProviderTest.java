package org.helyx.app.j2me.getabike.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.data.carto.CartoConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.provider.VelibStationContentProvider;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderException;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.config.BasicConfigurer;


public class VelibStationContentProviderTest extends TestCase {

	private static final Logger logger = Logger.getLogger("DEFAULT_STATION_CONTENT_PROVIDER_TEST");

	public VelibStationContentProviderTest() {
		super();
		new BasicConfigurer().configure();
	}
	
	public void testStationContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		
		logger.info("Coucou!");
		
		City city = new City();
		city.key = "SEVILLE";
		city.stationList = "/org/helyx/app/j2me/getabike/test/data/station/carto_seville.xml";
		
		IContentAccessor stationContentAccessor = new ClasspathContentAccessor(city.stationList);
		IContentProvider stationContentLoader = new VelibStationContentProvider(city, stationContentAccessor);
		
		final Vector stationList = new Vector();
		
		stationContentLoader.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {
			
			public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
				if (eventType == CartoConstants.ON_STATION_LOADED) {
					
						getLogger().info("ON_STATION_LOADED: " + eventData);
						assertEquals(Station.class.getName(), eventData.getClass().getName());
						stationList.addElement(eventData);
				}
			}

			public void onCancel(String eventMessage, Object eventData) {
				fail("Opération cancelled: " + eventMessage);
			}

			public void onError(String eventMessage, Object eventData) {
				fail("Error on opération: " + eventMessage);
			}

			public void onSuccess(String eventMessage, Object eventData) {
				getLogger().info( "ON_SUCCESS");
			}

		});
		
		stationContentLoader.loadData();
	}
	
}
