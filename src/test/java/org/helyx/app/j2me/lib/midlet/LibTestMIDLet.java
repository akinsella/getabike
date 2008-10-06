package org.helyx.app.j2me.lib.midlet;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.test.AsciiUtilTest;
import org.helyx.app.j2me.lib.test.DateFormatUtilTest;
import org.helyx.app.j2me.lib.test.DateUtilTest;
import org.helyx.app.j2me.lib.test.FastQuickSortTest;
import org.helyx.app.j2me.lib.test.MathUtilTest;
import org.helyx.app.j2me.lib.test.NumberFormatUtilTest;
import org.helyx.app.j2me.lib.test.ReflectTest;
import org.helyx.app.j2me.lib.test.TextUtilTest;

public class LibTestMIDLet extends cldcunit.runner.TestRunner {

        protected void startApp() {
            start(new TestCase[] {
            		new AsciiUtilTest(), 
            		new DateFormatUtilTest(),
            		new DateUtilTest(),
            		new FastQuickSortTest(),
            		new MathUtilTest(),
            		new NumberFormatUtilTest(),
            		new ReflectTest(),
            		new TextUtilTest()
            	});
        }

}