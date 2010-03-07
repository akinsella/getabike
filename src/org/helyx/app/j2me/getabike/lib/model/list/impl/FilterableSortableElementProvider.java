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

import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;
import org.helyx.app.j2me.getabike.lib.filter.Filter;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.model.list.IFilterableSortableElementProvider;
import org.helyx.app.j2me.getabike.lib.sort.FastQuickSort;

public class FilterableSortableElementProvider implements IElementProvider, IFilterableSortableElementProvider {

	protected IElementProvider rawElementProvider;
	protected Filter filter;
	protected Comparator comparator;
	protected Vector itemList = new Vector();
	

	public FilterableSortableElementProvider(IElementProvider rawElementProvider, Filter filter) {
		this(rawElementProvider, filter, null);
	}

	public FilterableSortableElementProvider(IElementProvider rawElementProvider, Comparator comparator) {
		this(rawElementProvider, null, comparator);
	}
	
	public FilterableSortableElementProvider(IElementProvider rawElementProvider, Filter filter, Comparator comparator) {
		super();
		this.rawElementProvider = rawElementProvider;
		this.filter = filter;
		this.comparator = comparator;
		filterAndSort();
	}
	
	public FilterableSortableElementProvider(IElementProvider rawElementProvider) {
		super();
		this.rawElementProvider = rawElementProvider;
		filterAndSort();
	}
	
	public IElementProvider getRawElementProvider() {
		return rawElementProvider;
	}
	
	public Object get(int offset) {
		return itemList.elementAt(offset);
	}

	public int length() {
		return itemList.size();
	}

	protected void filterAndSort() {
		int length = rawElementProvider.length();
		Vector itemList = new Vector(length);
		for (int i = 0 ; i < length ; i++) {
			Object item = rawElementProvider.get(i);
			if (filter == null || filter.matches(item)) {
				itemList.addElement(item);
			}
		}
		if (comparator != null) {
			int filteredListSize = itemList.size();
			Object[] items = new Object[filteredListSize];
			itemList.copyInto(items);
			FastQuickSort fastQuickSort = new FastQuickSort(comparator);
			fastQuickSort.sort(items);
			itemList = new Vector(filteredListSize);
			for (int i = 0 ; i < filteredListSize ; i++) {
				itemList.addElement(items[i]);
			}
		}
		this.itemList = itemList;
	}

	public Object[] getElements() {
		Object[] array = new Object[itemList.size()];
		itemList.copyInto(array);

		return array;
	}

}
