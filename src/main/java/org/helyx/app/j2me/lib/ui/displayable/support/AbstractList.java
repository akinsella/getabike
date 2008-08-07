package org.helyx.app.j2me.lib.ui.displayable.support;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public abstract class AbstractList extends AbstractDisplayable {
	
	protected List list;

	public AbstractList(AbstractMIDlet midlet, String title, int listType) {
		super(midlet);
		list = new List(title, listType);
	}

	public Displayable getDisplayable() {
		return list;
	}

	public List getList() {
		return list;
	}

	
	public void dispose() {
		list = null;
	}
}