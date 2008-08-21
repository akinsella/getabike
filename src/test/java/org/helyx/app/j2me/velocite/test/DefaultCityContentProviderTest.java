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
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;

public class DefaultCityContentProviderTest extends TestCase {

	private static final Log log = LogFactory.getLog("DEFAULT_CITY_CONTENT_PROVIDER_TEST");
	
	public void testCityContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/city_paris.xml");
		IContentProvider cityContentProvider = new DefaultCityContentProvider(cityContentAccessor);
		
		final Vector cityList = new Vector();
		
		cityContentProvider.addProgressListener(new ProgressAdapter() {

			public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
				if (eventType == CityConstants.ON_CITY_LOADED) {
					getLog().info("ON_CITY_LOADED: " + eventData);
					assertEquals(City.class.getName(), eventData.getClass().getName());
					cityList.addElement(eventData);
				}
			}

			public void onSuccess(String eventMessage, Object eventData) {
				getLog().info("ON_SUCCESS");
			}

			public void onError(String eventMessage, Object eventData) {
				fail("Error on opération: " + eventMessage);
			}
			
			public void onCancel(String eventMessage, Object eventData) {
				fail("Opération cancelled: " + eventMessage);
			}
			
		});
		
		cityContentProvider.loadData();
		assertEquals(1, cityList.size());
		City city = (City)cityList.elementAt(0);
		assertNotNull(city);
		assertEquals("PARIS", city.key);
		assertEquals("Paris", city.name);
		assertEquals("DEFAULT", city.type);
		assertEquals(true, city.active);
		assertEquals("http://www.velib.paris.fr/service/stationdetails/", city.stationDetails);
		assertEquals("http://www.velib.paris.fr/service/carto/", city.stationList);
		assertEquals("/carto_paris.xml", city.offlineStationList);
	}
	
	public void testCityContentLoader() throws ContentProviderException, ContentAccessorException {
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/cities.xml");
		IContentProvider cityContentLoader = new DefaultCityContentProvider(cityContentAccessor);
		
		final Vector cityList = new Vector();
		
		cityContentLoader.addProgressListener(new ProgressAdapter() {

			public void onCustomEvent(int eventType, String eventMessage, Object eventData) {
				if (eventType == CityConstants.ON_CITY_LOADED) {
					getLog().info("ON_CITY_LOADED: " + eventData);
					assertEquals(City.class.getName(), eventData.getClass().getName());
					cityList.addElement(eventData);
				}
			}

			public void onSuccess(String eventMessage, Object eventData) {
				getLog().info("ON_SUCCESS");
			}

			public void onError(String eventMessage, Object eventData) {
				fail("Error on opération: " + eventMessage);
			}
			
			public void onCancel(String eventMessage, Object eventData) {
				fail("Opération cancelled: " + eventMessage);
			}
			
		});
		
		cityContentLoader.loadData();
		assertTrue(cityList.size() > 0);
		
	}
	
}
