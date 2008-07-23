package org.helyx.app.j2me.lib.ui.widget.menu;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.ui.widget.ImageSet;

public class MenuItem {
	
	public static final int NO_ACCELERATOR = -1;
	
	private String title;
	private ImageSet imageSet;
	private int accelerator;
	private boolean active;
	private boolean enabled = true;
	private Object data;
	private IAction action;

	public MenuItem() {
		super();
	}

	public MenuItem(String title) {
		super();
		this.title = title;
	}

	public MenuItem(String title, IAction action) {
		super();
		this.title = title;
		this.action = action;
	}

	public MenuItem(String title, boolean enabled, IAction action) {
		super();
		this.title = title;
		this.action = action;
		this.enabled = enabled;
	}

	public MenuItem(String title, int accelerator, IAction action) {
		super();
		this.title = title;
		this.accelerator = accelerator;
		this.action = action;
	}

	public MenuItem(String title, int accelerator, boolean enabled) {
		super();
		this.title = title;
		this.accelerator = accelerator;
		this.enabled = enabled;
	}

	public MenuItem(String title, int accelerator, boolean enabled, IAction action) {
		super();
		this.title = title;
		this.accelerator = accelerator;
		this.action = action;
		this.enabled = enabled;
	}

	public MenuItem(String title, ImageSet imageSet, int accelerator, boolean enabled) {
		super();
		this.title = title;
		this.imageSet = imageSet;
		this.accelerator = accelerator;
		this.enabled = enabled;
	}

	public MenuItem(String title, ImageSet imageSet, int accelerator, boolean enabled, IAction action) {
		super();
		this.title = title;
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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

}
