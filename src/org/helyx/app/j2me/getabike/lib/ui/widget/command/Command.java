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
package org.helyx.app.j2me.getabike.lib.ui.widget.command;

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.renderer.text.ITextRenderer;


public class Command {

	private IAction action;
	private String text;
	private boolean interactive = true;
	
	protected ITextRenderer textRenderer;
	
	public Command() {
		super();
	}
	
	public Command(String text, ITextRenderer textRenderer, IAction action) {
		this(text, true, textRenderer, action);
	}
	
	public Command(String text, boolean interactive, ITextRenderer textRenderer, IAction action) {
		super();
		
		this.text = text;
		this.action = action;
		this.textRenderer = textRenderer;
		this.interactive = interactive;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(IAction action) {
		this.action = action;
	}
	
	public void setTextRenderer(ITextRenderer textRenderer) {
		this.textRenderer = textRenderer;
	}

	public String renderText() {
		String text = getText();
		return textRenderer != null ? textRenderer.renderText(text) : text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Command]");
		sb.append(" text='" + text + "'");
		
		String result = sb.toString();
		
		return result;
	}

}
