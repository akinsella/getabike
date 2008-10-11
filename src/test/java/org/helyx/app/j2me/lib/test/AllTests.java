package org.helyx.app.j2me.lib.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.helyx.app.j2me.lib.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(ReflectTest.class);
		suite.addTestSuite(DateUtilTest.class);
		suite.addTestSuite(AsciiUtilTest.class);
		suite.addTestSuite(FastQuickSortTest.class);
		suite.addTestSuite(NumberFormatUtilTest.class);
		suite.addTestSuite(DateFormatUtilTest.class);
		suite.addTestSuite(TextUtilTest.class);
		suite.addTestSuite(MathUtilTest.class);
		//$JUnit-END$
		return suite;
	}
	
	public void testSuite() {
		System.out.println("Test suite");
	}

}
