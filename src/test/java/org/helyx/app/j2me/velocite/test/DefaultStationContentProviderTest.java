package org.helyx.app.j2me.velocite.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.provider.DefaultStationContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class DefaultStationContentProviderTest extends TestCase {

	private static final String CAT = "DEFAULT_STATION_CONTENT_PROVIDER_TEST";
	
	public void testStationContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		City city = new City();
		city.key = "SEVILLE";
		city.stationList = "http://www.sevici.es/service/carto/";
		
		IContentAccessor stationContentAccessor = new HttpContentAccessor(city.stationList);
		IContentProvider stationContentLoader = new DefaultStationContentProvider(stationContentAccessor);
		
		final Vector stationList = new Vector();
		
		stationContentLoader.addProgressListener(new ProgressAdapter(CAT) {
			
			public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
				if (eventType == CartoConstants.ON_STATION_LOADED) {
					
						Log.info(getCat(), "ON_STATION_LOADED: " + eventData);
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
				Log.info(getCat(), "ON_SUCCESS");
			}

		});
		
		stationContentLoader.loadData();
	}
	
}
