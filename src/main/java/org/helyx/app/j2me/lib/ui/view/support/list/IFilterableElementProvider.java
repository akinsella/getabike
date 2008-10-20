package org.helyx.app.j2me.lib.ui.view.support.list;

import org.helyx.app.j2me.lib.filter.IObjectFilter;

public interface IFilterableElementProvider {

	IObjectFilter getFilter();
	void setObjectFilter(IObjectFilter filter);
	void filter();
	
}
