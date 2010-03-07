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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;

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
