package org.helyx.app.j2me.lib.test;


import junit.framework.TestCase;

import org.helyx.app.j2me.lib.comparator.Comparator;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.sort.FastQuickSort;

public class FastQuickSortTest extends TestCase {

	private static final Log log = LogFactory.getLog("FAST_QUICK_SORT_TEST");

	public void testFastQuickSort() {
		String[] values = new String[] { "aaa", "abd", "abc", "aa" };
		showStringList(values);
		
		log.info("In Values: " + showStringList(values));
		FastQuickSort fqs = new FastQuickSort(new Comparator() {

			public int compare(Object object1, Object object2) {
				return ((String)object1).compareTo((String)object2);
			}
			
		});
		
		fqs.sort(values);
		log.info("Out Values: " + showStringList(values));
		
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
