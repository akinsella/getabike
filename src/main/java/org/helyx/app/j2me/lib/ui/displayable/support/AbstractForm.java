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
		this.form = new Form(title);
	}

	public AbstractForm(AbstractMIDlet midlet, String title, Item[] items) {
		super(midlet);
		form = new Form(title, items);
	}
	
	public Displayable getDisplayable() {
		return form;
	}
	
	public Form getForm() {
		return form;
	}

}
