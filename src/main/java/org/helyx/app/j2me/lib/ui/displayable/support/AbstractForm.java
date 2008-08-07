package org.helyx.app.j2me.lib.ui.displayable.support;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public abstract class AbstractForm extends AbstractDisplayable {
	
	protected Form form;

	public AbstractForm(AbstractMIDlet midlet, String title) {
		super(midlet);
		form = new Form(title);
		form.setCommandListener(this);
	}

	public AbstractForm(AbstractMIDlet midlet, String title, Item[] items) {
		super(midlet);
		form = new Form(title, items);
		form.setCommandListener(this);
	}
	
	public Displayable getDisplayable() {
		return form;
	}
	
	public Form getForm() {
		return form;
	}

}
