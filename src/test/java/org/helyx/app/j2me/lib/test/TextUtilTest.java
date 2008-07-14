package org.helyx.app.j2me.lib.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.text.TextUtil;

public class TextUtilTest extends TestCase {

	public void testSplit() {
		String[] results = TextUtil.split("Coucou", 'c');
		assertEquals(1, results.length);
	}
	
}
