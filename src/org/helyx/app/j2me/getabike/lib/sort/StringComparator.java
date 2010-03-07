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
package org.helyx.app.j2me.getabike.lib.sort;

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;

public class StringComparator implements Comparator {

	public StringComparator() {
		super();
	}

	public int compare(Object object1, Object object2) {
		String str1 = (String)object1;
		String str2 = (String)object2;
		
		if (str1 == null && str2 == null) {
			return 0;
		}
		if (str1 == null) {
			return -1;
		}
		if (str2 == null) {
			return 1;
		}
		
		return str1.compareTo(str2);
	}

}
