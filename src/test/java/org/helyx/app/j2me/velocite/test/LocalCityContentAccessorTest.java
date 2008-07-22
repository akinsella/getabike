package org.helyx.app.j2me.velocite.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class LocalCityContentAccessorTest extends TestCase {

	private static final String CAT = "LOCAL_CITY_CONTENT_ACCESSOR_TEST";

	public void testLocalCityContentAccessor() throws ContentAccessorException, IOException {
		IContentAccessor cityContentAccessor = new ClasspathContentAccessor("/cities.xml");
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
