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

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.util.Null;

public class MenuItem {

	public static final int NO_ACCELERATOR = -1;
	
	private String text;
	private ImageSet imageSet;
	private int accelerator;
	private boolean active;
	private boolean enabled = true;
	private Object data;
	private Hashtable dataMap;
	private IAction action;
	private boolean parentMenu;

	public MenuItem() {
		super();
	}

	public MenuItem(String text) {
		super();
		this.text = text;
	}

	public MenuItem(String text, IAction action) {
		super();
		this.text = text;
		this.action = action;
	}

	public MenuItem(String text, boolean enabled, IAction action) {
		super();
		this.text = text;
		this.action = action;
		this.enabled = enabled;
	}

	public MenuItem(String text, int accelerator, IAction action) {
		super();
		this.text = text;
		this.accelerator = accelerator;
		this.action = action;
	}

	public MenuItem(String text, int accelerator, boolean enabled) {
		super();
		this.text = text;
		this.accelerator = accelerator;
		this.enabled = enabled;
	}

	public MenuItem(String text, ImageSet imageSet, IAction action) {
		super();
		this.text = text;
		this.imageSet = imageSet;
		this.action = action;
	}

	public MenuItem(String text, int accelerator, boolean enabled, IAction action) {
		super();
		this.text = text;
		this.accelerator = accelerator;
		this.action = action;
		this.enabled = enabled;
	}

	public MenuItem(String text, ImageSet imageSet, int accelerator, boolean enabled) {
		super();
		this.text = text;
		this.imageSet = imageSet;
		this.accelerator = accelerator;
		this.enabled = enabled;
	}

	public MenuItem(String text, ImageSet imageSet, int accelerator, boolean enabled, IAction action) {
		super();
		this.text = text;
		this.imageSet = imageSet;
		this.accelerator = accelerator;
		this.enabled = enabled;
		this.action = action;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public IAction getAction() {
		return action;
	}
	
	public void setAction(IAction action) {
		this.action = action;
	}

	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public ImageSet getImageSet() {
		return imageSet;
	}
	
	public void setImageSet(ImageSet imageSet) {
		this.imageSet = imageSet;
	}
	
	public int getAccelerator() {
		return accelerator;
	}
	
	public void setAccelerator(int accelerator) {
		this.accelerator = accelerator;
	}

	public void setParentMenu(boolean parentMenu) {
		this.parentMenu = parentMenu;
	}

	public boolean isParentMenu() {
		return parentMenu;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	private void checkDataMap() {
		if (dataMap == null) {
			dataMap = new Hashtable();
		}
	}
	
	public void setData(String key, Object value) {
		checkDataMap();
		dataMap.put(key, value != null ? value : Null.getInstance());
	}
	
	public Object getData(String key) {
		checkDataMap();
		Object resultObject = dataMap.get(key);
		
		if (resultObject instanceof Null) {
			return null;
		}
		
		return resultObject;
	}

	public void removeData(String key) {
		checkDataMap();
		dataMap.remove(key);
	}
	
	public void clearAllData() {
		checkDataMap();
		dataMap.clear();
	}

}
