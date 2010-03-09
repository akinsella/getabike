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
package org.helyx.app.j2me.getabike.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.ViewCanvasZone;
import org.helyx.logging4me.Logger;


public abstract class AbstractViewCanvasZone implements ViewCanvasZone {

	private static final Logger logger = Logger.getLogger("ABSTRACT_VIEW_CANVAS_ZONE");
	
	protected AbstractView view;
	
	public AbstractViewCanvasZone(AbstractView view) {
		super();
		this.view = view;
	}
	
	public AbstractView getView() {
		return view;
	}

	public abstract Rectangle computeArea();

	public abstract void paintArea(Graphics graphics);
	
}