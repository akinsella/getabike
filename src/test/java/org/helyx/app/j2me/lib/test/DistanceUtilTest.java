package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.math.DistanceUtil;

public class DistanceUtilTest extends TestCase {
	
	private static final Logger logger = LoggerFactory.getLogger("DISTANCE_UTIL_TEST");
	
	public void testDistance() {
		System.out.println(DistanceUtil.distance(47.2471819477, 6.02276834043, 47.2328969915, 6.0203034105, DistanceUtil.KM) * 1000);
	}
	
}
