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

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.lib.text.StringFormat;

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
