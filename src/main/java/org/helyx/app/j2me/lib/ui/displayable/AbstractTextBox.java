package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public abstract class AbstractTextBox extends AbstractDisplayable {
	
	protected TextBox textBox;

	public String getTitle() {
		return textBox.getTitle();
	}

	public boolean isFullScreenMode() {
		return false;
	}

	public AbstractTextBox(MIDlet midlet, String title) {
		super(midlet);
		this.textBox = new TextBox(title, "", 128, TextField.ANY);
		textBox.setCommandListener(this);
	}
	
	public Displayable getDisplayable() {
		return textBox;
	}
	
	public TextBox getTextBox() {
		return textBox;
	}

}
