package org.helyx.app.j2me.lib.ui.widget.menu;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.ui.widget.ImageSet;

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
		dataMap.put(key, value);
	}
	
	public Object getData(String key) {
		checkDataMap();
		Object resultObject = dataMap.get(key);
		
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
