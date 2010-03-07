package org.helyx.app.j2me.getabike.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.config.BasicConfigurer;


public class LocalCityContentAccessorTest extends TestCase {

	private static final Logger logger = Logger.getLogger("LOCAL_CITY_CONTENT_ACCESSOR_TEST");

	public LocalCityContentAccessorTest() {
		super();
		new BasicConfigurer().configure();
	}

	public void testLocalCityContentAccessor() throws ContentAccessorException, IOException {
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/org/helyx/app/j2me/getabike/data/city/cities.xml");
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
