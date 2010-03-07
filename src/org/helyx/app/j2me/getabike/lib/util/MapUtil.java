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
package org.helyx.app.j2me.getabike.lib.util;

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
