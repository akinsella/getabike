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
package org.helyx.app.j2me.getabike.lib.ui.widget.menu;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class Menu {	
	
	private static final Logger logger = Logger.getLogger(Menu.class);
	
	protected Vector menuItems;
	
	protected MenuItem selectedMenuItem;
	
	protected MenuItem checkedMenuItem;

	public Menu() {
		super();
		menuItems = new Vector();
	}
	
	public MenuItem getCheckedMenuItem() {
		return checkedMenuItem;
	}

	public void setCheckedMenuItem(MenuItem checkedMenuItem) {
		this.checkedMenuItem = checkedMenuItem;
	}
	
	public Enumeration menuItemEnumeration() {
		return menuItems.elements();
	}
	
	public void addMenuItem(MenuItem menuItem) {
		menuItems.addElement(menuItem);
	}
	
	public void removeMenuItem(MenuItem menuItem) {
		menuItems.removeElement(menuItem);
	}
	
	public void removeAllMenuItems() {
		menuItems.removeAllElements();
	}

	public MenuItem getSelectedMenuItem() {
		if (!menuItems.contains(selectedMenuItem)) {
			selectedMenuItem = null;
		}
		return selectedMenuItem;
	}

	public void setSelectedMenuItem(MenuItem selectedMenuItem) {
		if (!menuItems.contains(selectedMenuItem)) {
			this.selectedMenuItem = null;
		}
		else {
			this.selectedMenuItem = selectedMenuItem;
		}
	}

	public int getSelectedMenuItemIndex() {
		if (selectedMenuItem == null) {
			return -1;
		}
		int size = menuItems.size();
		for (int i = 0 ; i < size ; i++) {
			if (menuItems.elementAt(i) == selectedMenuItem) {
				return i;
			}
		}
		
		return -1;
	}

	public int menuItemCount() {
		int size = menuItems.size();
		
		return size;
	}
	

	public MenuItem getMenuItem(int index) {

		int count = 0;
		int size = menuItems.size();
		for (int i = 0 ; i < size ; i++) {
				if (count == index) {
					MenuItem menuItem = (MenuItem)menuItems.elementAt(i);
					return menuItem;
				}
				count++;
		}
		
		return null;
	}

	public void setSelectedMenuItemIndex(int menuItemIndex) {

		int count = 0;
		int size = menuItems.size();
		for (int i = 0 ; i < size ; i++) {
			if (count == menuItemIndex) {
				selectedMenuItem = (MenuItem)menuItems.elementAt(i);
			}
			count++;
		}
	}

	public Vector getMenuItems() {
		return menuItems;
	}

	
}
