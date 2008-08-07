package org.helyx.app.j2me.lib.ui.displayable.support;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public abstract class AbstractTextBox extends AbstractDisplayable {
	
	protected TextBox textBox;

	public String getTitle() {
		return textBox.getTitle();
	}

	public boolean isFullScreenMode() {
		return false;
	}

	public AbstractTextBox(AbstractMIDlet midlet, String title) {
		super(midlet);
		textBox = new TextBox(title, "", 128, TextField.ANY);
		textBox.setCommandListener(this);
	}
	
	public Displayable getDisplayable() {
		return textBox;
	}
	
	public TextBox getTextBox() {
		return textBox;
	}

}
