package org.helyx.app.j2me.lib.ui.view.support.list;

import java.util.Vector;

import org.helyx.app.j2me.lib.filter.IObjectFilter;

public class DynamicFilterableElementProvider extends FilterableElementProvider implements IFilterableElementProvider {

	private IElementProvider elementProvider;
	private IObjectFilter objectFilter;
	private Vector itemList = new Vector();
	
	public DynamicFilterableElementProvider(IElementProvider elementProvider, IObjectFilter objectFilter) {
		super(elementProvider, objectFilter);
		this.elementProvider = elementProvider;
		this.objectFilter = objectFilter;
		filter();
	}
	
	public DynamicFilterableElementProvider(IElementProvider elementProvider) {
		super(elementProvider);
		this.elementProvider = elementProvider;
		filter();
	}
	
	public void filter() {
		super.filter();
	}

	public Object get(int offset) {
		return itemList.elementAt(offset);
	}

	public int length() {
		return itemList.size();
	}
	
	public IObjectFilter getFilter() {
		return objectFilter;
	}

	public void setObjectFilter(IObjectFilter filter) {
		this.objectFilter = objectFilter;
	}

}
