package org.helyx.app.j2me.lib.ui.view.support.list;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IObjectFilter;

public class FilterableElementProvider implements IElementProvider, IFilterableElementProvider {

	protected IElementProvider elementProvider;
	protected IObjectFilter objectFilter;
	protected Vector itemList = new Vector();
	
	public FilterableElementProvider(IElementProvider elementProvider, IObjectFilter objectFilter) {
		super();
		this.elementProvider = elementProvider;
		this.objectFilter = objectFilter;
		filter();
	}
	
	public FilterableElementProvider(IElementProvider elementProvider) {
		super();
		this.elementProvider = elementProvider;
		filter();
	}
	
	public IElementProvider getUnfilteredElements() {
		return elementProvider;
	}
	
	public Object get(int offset) {
		return itemList.elementAt(offset);
	}

	public int length() {
		return itemList.size();
	}

	protected void filter() {
		int length = elementProvider.length();
		Vector itemList = new Vector(length);
		for (int i = 0 ; i < length ; i++) {
			Object item = elementProvider.get(i);
			if (objectFilter.matches(item)) {
				itemList.addElement(item);
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
