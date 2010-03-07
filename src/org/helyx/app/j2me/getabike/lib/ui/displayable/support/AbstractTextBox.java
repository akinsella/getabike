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
package org.helyx.app.j2me.getabike.lib.ui.displayable.support;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;

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
	
	public void setTitle(String title) {
		textBox.setTitle(title);
	}
	
	public Displayable getDisplayable() {
		return textBox;
	}
	
	public TextBox getTextBox() {
		return textBox;
	}

}
