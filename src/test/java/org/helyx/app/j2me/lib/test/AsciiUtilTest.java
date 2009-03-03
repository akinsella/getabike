package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.text.AsciiUtil;

public class AsciiUtilTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger("ASCII_UTIL_TEST");
	
	public void testGenerateHexaString() throws Exception {
		AsciiUtil.generateHexaString();
	}

	public void testGenerateHexaStringWithSizeZero() throws Exception {
		try {
			AsciiUtil.generateHexaString(0);
			fail("Should throw an exception");
		}
		catch(Exception e) {
			logger.info("Error handled: " + e.getMessage());
		}
	}

	public void testGenerateHexaStringWithSizeOne() throws Exception {
		String hexaString = AsciiUtil.generateHexaString(1);
		logger.info("Hexa String with size one generated: " + hexaString);
		assertNotNull(hexaString);
		assertEquals(1, hexaString.length());
	}

	public void testGenerateHexaStringWithSizeTen() throws Exception {
		String hexaString = AsciiUtil.generateHexaString(10);
		logger.info("Hexa String with size ten generated: " + hexaString);
		assertNotNull(hexaString);
		assertEquals(10, hexaString.length());
	}
	
}
