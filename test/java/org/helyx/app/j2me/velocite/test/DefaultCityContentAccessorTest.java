package org.helyx.app.j2me.velocite.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.helyx.helyx4me.content.accessor.ClasspathContentAccessor;
import org.helyx.helyx4me.content.accessor.ContentAccessorException;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.config.BasicConfigurer;


public class DefaultCityContentAccessorTest extends TestCase {

	private static final Logger logger = Logger.getLogger("DEFAULT_CITY_CONTENT_ACCESSOR_TEST");

	public DefaultCityContentAccessorTest() {
		super();
		new BasicConfigurer().configure();
	}
 
	public void testDefaultCityContentAccessor() throws ContentAccessorException, IOException {

		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/data/city/cities.xml");
		InputStreamProvider inputStreamProvider = cityContentAccessor.getInputStreamProvider();
		
		assertNotNull(inputStreamProvider);
		
		assertFalse(inputStreamProvider.isCreated());
		assertFalse(inputStreamProvider.isDisposed());

		InputStream is = inputStreamProvider.createInputStream();
		
		assertTrue(inputStreamProvider.isCreated());
		assertFalse(inputStreamProvider.isDisposed());
		
		assertNotNull(is);
		
		inputStreamProvider.dispose();
		
		assertTrue(inputStreamProvider.isCreated());
		assertTrue(inputStreamProvider.isDisposed());
	}

}
