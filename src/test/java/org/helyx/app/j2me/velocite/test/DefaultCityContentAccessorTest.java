package org.helyx.app.j2me.velocite.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.stream.IInputStreamProvider;

public class DefaultCityContentAccessorTest extends TestCase {

	private static final String CAT = "DEFAULT_CITY_CONTENT_ACCESSOR_TEST";


	public void testDefaultCityContentAccessor() throws ContentAccessorException, IOException {

		IContentAccessor cityContentAccessor = new HttpContentAccessor("http://www.velocite.org/cities.xml");
		IInputStreamProvider inputStreamProvider = cityContentAccessor.getInputStreamProvider();
		
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
