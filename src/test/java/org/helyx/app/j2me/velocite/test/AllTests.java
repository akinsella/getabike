package org.helyx.app.j2me.velocite.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.helyx.app.j2me.velocite.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(DefaultStationContentProviderTest.class);
		suite.addTestSuite(DefaultCityContentProviderTest.class);
		suite.addTest(AllTests.suite());
		suite.addTestSuite(DefaultCityContentAccessorTest.class);
		suite.addTestSuite(LocalCityContentAccessorTest.class);
		//$JUnit-END$
		return suite;
	}

	public void testSuite() {
		System.out.println("Test suite");
	}

}
