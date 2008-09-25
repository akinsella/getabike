package org.helyx.app.j2me.velocite.test;

import java.util.Vector;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;

public class DefaultCityContentProviderTest extends TestCase {

	private static final Log log = LogFactory.getLog("DEFAULT_CITY_CONTENT_PROVIDER_TEST");

	public DefaultCityContentProviderTest() {
		super();
	}

	public void testCityContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/test/data/city/city_paris.xml");
		IContentProvider cityContentProvider = new DefaultCityContentProvider(cityContentAccessor);
	
		Vector cityList = (Vector)Future.get(new ContentProviderProgressTaskAdapter(cityContentProvider));

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
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/city/cities.xml");
		IContentProvider cityContentProvider = new DefaultCityContentProvider(cityContentAccessor);
				
		Vector cityList = (Vector)Future.get(new ContentProviderProgressTaskAdapter(cityContentProvider));

		assertTrue(cityList.size() > 0);
	}
	
}
