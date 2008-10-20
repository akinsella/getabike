package org.helyx.app.j2me.lib.filter;

import java.util.Vector;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ObjectFilterChain implements IObjectFilter {
	
	private static final Log log = LogFactory.getLog("RECORD_FILTER_CHAIN");

	Vector chainList = new Vector();
	
	public ObjectFilterChain() {
		super();
	}
	
	public void addFilter(IRecordFilter recordFilter) {
		chainList.addElement(recordFilter);
	}
	
	public void removeFilter(IRecordFilter recordFilter) {
		chainList.removeElement(recordFilter);
	}
	
	public void removeAllFilters() {
		chainList.removeAllElements();
	}
	
	public boolean matches(Object object) {
		int length = chainList.size();
		for (int i = 0 ; i < length ; i++) {
			IObjectFilter recordFilter = (IObjectFilter)chainList.elementAt(i);
			if (!recordFilter.matches(object)) {
				return false;
			}
		}
		
		return true;
	}
	
}
