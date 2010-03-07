/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.test;



import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.lib.text.AsciiUtil;
import org.helyx.logging4me.Logger;


public class AsciiUtilTest extends TestCase {

	private static final Logger logger = Logger.getLogger("ASCII_UTIL_TEST");
	
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
