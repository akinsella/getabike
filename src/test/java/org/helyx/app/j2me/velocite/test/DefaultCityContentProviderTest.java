package org.helyx.app.j2me.velocite.test;

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;

import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;
import org.helyx.helyx4me.concurrent.Future;
import org.helyx.helyx4me.content.accessor.ClasspathContentAccessor;
import org.helyx.helyx4me.content.accessor.ContentAccessorException;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.helyx4me.content.provider.IContentProvider;
import org.helyx.helyx4me.content.provider.exception.ContentProviderException;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;

public class DefaultCityContentProviderTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger("DEFAULT_CITY_CONTENT_PROVIDER_TEST");

	public DefaultCityContentProviderTest() {
		super();
	}

	public void testCityContentLoaderParis() throws ContentProviderException, ContentAccessorException {
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/city/cities.xml");
		IContentProvider cityContentProvider = new DefaultCityContentProvider(cityContentAccessor);
	
		Vector cityList = (Vector)Future.get(new ContentProviderProgressTaskAdapter(cityContentProvider));
		Enumeration _enum = cityList.elements();
		
		while (_enum.hasMoreElements()) {
			
			City city = (City)_enum.nextElement();
			if ("PARIS".equals(city.key)) {
				assertNotNull(city);
				assertEquals("PARIS", city.key);
				assertEquals("Paris", city.name);
				assertEquals("Vélib'", city.serviceName);
				assertEquals("VELIB", city.type);
				assertEquals(true, city.active);
				assertEquals("http://www.velib.paris.fr/service/stationdetails/", city.stationDetails);
				assertEquals("http://www.velib.paris.fr/service/carto/", city.stationList);
			}
			
			logger.info(city);
		}
	}
	
	public void testCityContentLoader() throws ContentProviderException, ContentAccessorException {
		
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/city/cities.xml");
		IContentProvider cityContentProvider = new DefaultCityContentProvider(cityContentAccessor);
				
		Vector cityList = (Vector)Future.get(new ContentProviderProgressTaskAdapter(cityContentProvider));

		assertTrue(cityList.size() > 0);
	}
	
}
