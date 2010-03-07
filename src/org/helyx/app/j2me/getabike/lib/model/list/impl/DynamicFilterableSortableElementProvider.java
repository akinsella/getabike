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
package org.helyx.app.j2me.getabike.lib.model.list.impl;

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;
import org.helyx.app.j2me.getabike.lib.filter.Filter;
import org.helyx.app.j2me.getabike.lib.model.list.IDynamicFilterableSortableElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.impl.FilterableSortableElementProvider;

public class DynamicFilterableSortableElementProvider extends FilterableSortableElementProvider implements IDynamicFilterableSortableElementProvider {
	
	public DynamicFilterableSortableElementProvider(IElementProvider elementProvider, Filter filter, Comparator comparator) {
		super(elementProvider, filter, comparator);
	}
	
	public DynamicFilterableSortableElementProvider(IElementProvider elementProvider) {
		super(elementProvider);
	}
	
	public void filterAndSort() {
		super.filterAndSort();
	}
	
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

}
