package org.helyx.app.j2me.velocite.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.DefaultStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class DefaultStationContentProviderTest extends TestCase {

	private static final Log log = LogFactory.getLog("DEFAULT_STATION_CONTENT_PROVIDER_TEST");

	public DefaultStationContentProviderTest() {
		super();
	}
	
	public void testStationContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		City city = new City();
		city.key = "SEVILLE";
		city.stationList = "/org/helyx/app/j2me/velocite/test/data/station/carto_seville.xml";
		
		IContentAccessor stationContentAccessor = new ClasspathContentAccessor(city.stationList);
		IContentProvider stationContentLoader = new DefaultStationContentProvider(stationContentAccessor);
		
		final Vector stationList = new Vector();
		
		stationContentLoader.addProgressListener(new ProgressAdapter() {
			
			public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
				if (eventType == CartoConstants.ON_STATION_LOADED) {
					
						getLog().info("ON_STATION_LOADED: " + eventData);
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
				getLog().info( "ON_SUCCESS");
			}

		});
		
		stationContentLoader.loadData();
	}
	
}
