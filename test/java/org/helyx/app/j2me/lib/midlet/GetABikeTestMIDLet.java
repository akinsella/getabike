package org.helyx.app.j2me.lib.midlet;

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.test.DefaultCityContentAccessorTest;
import org.helyx.app.j2me.getabike.test.DefaultCityContentProviderTest;
import org.helyx.app.j2me.getabike.test.LocalCityContentAccessorTest;
import org.helyx.app.j2me.getabike.test.VelibStationContentProviderTest;

public class GetABikeTestMIDLet extends cldcunit.runner.TestRunner {

        protected void startApp() {
            start(new TestCase[] {
            		new DefaultCityContentAccessorTest(), 
            		new DefaultCityContentProviderTest(),
            		new VelibStationContentProviderTest(),
            		new LocalCityContentAccessorTest()
            	});
        }

}