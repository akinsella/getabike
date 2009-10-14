package org.helyx.app.j2me.getabike.ui.view.station.search;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.renderer.text.DefaultTextRenderer;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;

public class StationSearchZipCodeFilterView extends MenuListView {

	private Vector zipCodeList;
	
	public StationSearchZipCodeFilterView(AbstractMIDlet midlet, Vector zipCodeList) {
		super(midlet, "view.station.search.filter.zipcode.tilte", false);
		this.zipCodeList = zipCodeList;
		init();
	}
	
	private void init() {
		setItemTextRenderer(new DefaultTextRenderer());

		Enumeration zipCodeListEnum = zipCodeList.elements();
		while (zipCodeListEnum.hasMoreElements()) {
			String zipCode = (String)zipCodeListEnum.nextElement();
			MenuItem menuItem = new MenuItem(zipCode);
			getMenu().addMenuItem(menuItem);
		}
	}

}
