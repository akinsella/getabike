package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.text.AsciiUtil;

public class AsciiUtilTest extends TestCase {

	private static final String CAT = "ASCII_UTIL_TEST";
	
	public void testGenerateHexaString() throws Exception {
		AsciiUtil.generateHexaString();
	}

	public void testGenerateHexaStringWithSizeZero() throws Exception {
		try {
			AsciiUtil.generateHexaString(0);
			fail("Should throw an exception");
		}
		catch(Exception e) {
			Log.info(CAT, "Error handled: " + e.getMessage());
		}
	}

	public void testGenerateHexaStringWithSizeOne() throws Exception {
		String hexaString = AsciiUtil.generateHexaString(1);
		Log.info(CAT, "Hexa String with size one generated: " + hexaString);
		assertNotNull(hexaString);
		assertEquals(1, hexaString.length());
	}

	public void testGenerateHexaStringWithSizeTen() throws Exception {
		String hexaString = AsciiUtil.generateHexaString(10);
		Log.info(CAT, "Hexa String with size ten generated: " + hexaString);
		assertNotNull(hexaString);
		assertEquals(10, hexaString.length());
	}
	
}
