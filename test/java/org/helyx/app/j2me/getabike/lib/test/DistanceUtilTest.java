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

import org.helyx.app.j2me.getabike.lib.math.DistanceUtil;
import org.helyx.logging4me.Logger;


public class DistanceUtilTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger("DISTANCE_UTIL_TEST");
	
	public void testDistance() {
		System.out.println(DistanceUtil.distance(47.2471819477, 6.02276834043, 47.2328969915, 6.0203034105, DistanceUtil.KM) * 1000);
	}
	
}
