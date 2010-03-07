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

import org.helyx.app.j2me.getabike.lib.text.TextUtil;

public class TextUtilTest extends TestCase {

	public void testSplitWord() {
		String[] results = TextUtil.split("Coucou", 'c');
		assertEquals(2, results.length);
		assertEquals("Cou", results[0]);
		assertEquals("ou", results[1]);
	}

	public void testSplitVersion() {
		String[] results = TextUtil.split("1.2.7", '.');
		assertEquals(3, results.length);
		assertEquals("1", results[0]);
		assertEquals("2", results[1]);
		assertEquals("7", results[2]);
	}
	
}
