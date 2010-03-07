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
package org.helyx.app.j2me.getabike.lib.ui.view.support.menu;

import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;

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
