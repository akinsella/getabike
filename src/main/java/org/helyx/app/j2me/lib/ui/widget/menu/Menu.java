package org.helyx.app.j2me.lib.ui.widget.menu;

import java.util.Enumeration;
import java.util.Vector;


public class Menu {	
	
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
					return (MenuItem)menuItems.elementAt(i);
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

	
}
