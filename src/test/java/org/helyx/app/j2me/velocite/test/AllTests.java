package org.helyx.app.j2me.velocite.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.helyx.app.j2me.velocite.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(LocalCityContentAccessorTest.class);
		suite.addTestSuite(DefaultStationContentProviderTest.class);
		suite.addTestSuite(DefaultCityContentProviderTest.class);
		suite.addTestSuite(DefaultCityContentAccessorTest.class);
		//$JUnit-END$
		return suite;
	}

}
