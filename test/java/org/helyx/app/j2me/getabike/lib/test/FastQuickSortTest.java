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

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;
import org.helyx.app.j2me.getabike.lib.sort.FastQuickSort;
import org.helyx.logging4me.Logger;


public class FastQuickSortTest extends TestCase {

	private static final Logger logger = Logger.getLogger("FAST_QUICK_SORT_TEST");

	public void testFastQuickSort() {
		String[] values = new String[] { "aaa", "abd", "abc", "aa" };
		showStringList(values);
		
		logger.info("In Values: " + showStringList(values));
		FastQuickSort fqs = new FastQuickSort(new Comparator() {

			public int compare(Object object1, Object object2) {
				return ((String)object1).compareTo((String)object2);
			}
			
		});
		
		fqs.sort(values);
		logger.info("Out Values: " + showStringList(values));
		
		assertEquals(4, values.length);
		assertEquals("aa", values[0]);
		assertEquals("aaa", values[1]);
		assertEquals("abc", values[2]);
		assertEquals("abd", values[3]);
	}

	private String showStringList(String[] values) {
		StringBuffer sb = new StringBuffer();
		int length = values.length;
		sb.append("[");
		for (int i = 0 ; i < length ; i++) {
			sb.append(values[i]);
			if (i + 1 < length) {
				sb.append(";");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}
	
}
