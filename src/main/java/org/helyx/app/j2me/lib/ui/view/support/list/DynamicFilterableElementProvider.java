package org.helyx.app.j2me.lib.ui.view.support.list;

import org.helyx.app.j2me.lib.filter.IObjectFilter;

public class DynamicFilterableElementProvider extends FilterableElementProvider implements IDynamicFilterableElementProvider {
	
	public DynamicFilterableElementProvider(IElementProvider elementProvider, IObjectFilter objectFilter) {
		super(elementProvider, objectFilter);
	}
	
	public DynamicFilterableElementProvider(IElementProvider elementProvider) {
		super(elementProvider);
	}
	
	public void filter() {
		super.filter();
	}
	
	public IObjectFilter getFilter() {
		return objectFilter;
	}

	public void setObjectFilter(IObjectFilter objectFilter) {
		this.objectFilter = objectFilter;
	}

}
