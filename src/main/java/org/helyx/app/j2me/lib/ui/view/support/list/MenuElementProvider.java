package org.helyx.app.j2me.lib.ui.view.support.list;

import org.helyx.app.j2me.lib.ui.widget.menu.Menu;

public class MenuElementProvider implements IElementProvider {

	private Menu menu;
	
	public MenuElementProvider(Menu menu) {
		super();
		this.menu = menu;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public Object get(int offset) {
		return menu.getMenuItem(offset);
	}

	public int length() {
		return menu.menuItemCount();
	}

	public Object[] getElements() {
		Object[] array = new Object[menu.menuItemCount()];
		menu.getMenuItems().copyInto(array);
		return array;
	}

}
