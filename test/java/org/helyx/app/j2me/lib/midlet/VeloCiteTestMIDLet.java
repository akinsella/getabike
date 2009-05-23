package org.helyx.app.j2me.lib.midlet;

import junit.framework.TestCase;

import org.helyx.app.j2me.velocite.test.DefaultCityContentAccessorTest;
import org.helyx.app.j2me.velocite.test.DefaultCityContentProviderTest;
import org.helyx.app.j2me.velocite.test.DefaultStationContentProviderTest;
import org.helyx.app.j2me.velocite.test.LocalCityContentAccessorTest;

public class VeloCiteTestMIDLet extends cldcunit.runner.TestRunner {

        protected void startApp() {
            start(new TestCase[] {
            		new DefaultCityContentAccessorTest(), 
            		new DefaultCityContentProviderTest(),
            		new DefaultStationContentProviderTest(),
            		new LocalCityContentAccessorTest()
            	});
        }

}