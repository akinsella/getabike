package org.helyx.app.j2me.lib.ui.view.support.list;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IObjectFilter;

public class FilterableElementProvider implements IElementProvider {

	private IElementProvider elementProvider;
	private IObjectFilter objectFilter;
	private Vector itemList = new Vector();
	
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
				continue;
			}
			itemList.addElement(item);
		}
		this.itemList = itemList;
	}

}
