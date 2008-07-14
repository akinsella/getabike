package org.helyx.app.j2me.lib.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.helyx.app.j2me.lib.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(DateUtilTest.class);
		suite.addTestSuite(MathUtilTest.class);
		suite.addTestSuite(NumberFormatUtilTest.class);
		suite.addTestSuite(AsciiUtilTest.class);
		suite.addTestSuite(FastQuickSortTest.class);
		suite.addTestSuite(DateFormatUtilTest.class);
		suite.addTestSuite(TextUtilTest.class);
		//$JUnit-END$
		return suite;
	}

}
