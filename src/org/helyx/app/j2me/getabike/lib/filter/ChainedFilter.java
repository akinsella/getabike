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
package org.helyx.app.j2me.getabike.lib.filter;

import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.filter.Filter;
import org.helyx.logging4me.Logger;


public class ChainedFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger("RECORD_FILTER_CHAIN");

	Vector chainList = new Vector();
	
	public ChainedFilter() {
		super();
	}
	
	public void addFilter(Filter recordFilter) {
		chainList.addElement(recordFilter);
	}
	
	public void removeFilter(Filter recordFilter) {
		chainList.removeElement(recordFilter);
	}
	
	public void removeAllFilters() {
		chainList.removeAllElements();
	}
	
	public boolean matches(Object object) {
		int length = chainList.size();
		for (int i = 0 ; i < length ; i++) {
			Filter recordFilter = (Filter)chainList.elementAt(i);
			if (!recordFilter.matches(object)) {
				return false;
			}
		}
		
		return true;
	}
	
}
