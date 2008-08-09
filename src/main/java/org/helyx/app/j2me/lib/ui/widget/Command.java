package org.helyx.app.j2me.lib.ui.widget;

import org.helyx.app.j2me.lib.action.IAction;


public class Command {

	private IAction action;
	private String text;
	private boolean enabled = true;
	
	public Command() {
		super();
	}
	
	public Command(String text, IAction action) {
		this(text, true, action);
	}
	
	public Command(String text, boolean enabled, IAction action) {
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
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Command]");
		sb.append(" text='" + text + "'");
		sb.append(", enabled=" + enabled);
		
		String result = sb.toString();
		
		return result;
	}

}
