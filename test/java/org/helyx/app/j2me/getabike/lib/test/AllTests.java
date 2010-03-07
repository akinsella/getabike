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

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.helyx.app.j2me.getabike.lib.test");
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
