package org.helyx.app.j2me.lib.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.text.StringFormat;

public class StringFormatTest extends TestCase {

	public void testOneParameter() {
		StringFormat sf = new StringFormat("http://www.velib.paris.fr/service/stationdetails/{1}");
		String result = sf.format("1032");
		assertEquals("http://www.velib.paris.fr/service/stationdetails/1032", result);
	}

	public void testTwoParameters() {
		StringFormat sf = new StringFormat("http://www.velov.grandlyon.com/velovmap/zhp/inc/DispoStationsParId.php?id={1}&noCache={2}");
		String result = sf.format(new Object[] { "1032", "10923" });
		assertEquals("http://www.velov.grandlyon.com/velovmap/zhp/inc/DispoStationsParId.php?id=1032&noCache=10923", result);
	}
	
}
