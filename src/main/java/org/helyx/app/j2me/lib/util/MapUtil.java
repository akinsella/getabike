package org.helyx.app.j2me.lib.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class MapUtil {

	public static Hashtable duplicate(Hashtable hashtable) {
		Hashtable newHt = new Hashtable();
		Enumeration _enum = hashtable.keys();
		while (_enum.hasMoreElements()) {
			Object key = (Object)_enum.nextElement();
			newHt.put(key, hashtable.get(key));
		}
		
		return newHt;
	}

}
