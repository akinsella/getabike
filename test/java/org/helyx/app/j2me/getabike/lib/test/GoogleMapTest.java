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

import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.map.google.GoogleMaps;
import org.helyx.logging4me.Logger;


public class GoogleMapTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger("GOOGLE_MAP_TEST");

	public GoogleMapTest() {
		super();
	}
	
	public void testGoogleMapLocalizationScrollLeft() {
		GoogleMaps googleMaps = new GoogleMaps("ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA");
		Point localization = new Point(48.8610789316, 2.40036207675);
		logger.info("Localization: " + localization);
		Point newLocalization = googleMaps.adjust(localization, 0, 0, 14);
		logger.info("New X localization: " + newLocalization);

		Point newLocalization2 = googleMaps.adjust(localization, 0, 0, 19);
		logger.info("New Y localization: " + newLocalization2);
	}
	
}
