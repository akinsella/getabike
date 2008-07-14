package org.helyx.app.j2me.lib.ui.widget.action;

import org.helyx.app.j2me.lib.ui.widget.IAction;

public class ActionItem {

	private IAction action;
	private String text;
	private boolean enabled = true;
	
	public ActionItem() {
		super();
	}
	
	public ActionItem(String text, boolean enabled, IAction action) {
		super();
		
		this.text = text;
		this.enabled = enabled;
		this.action = action;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
