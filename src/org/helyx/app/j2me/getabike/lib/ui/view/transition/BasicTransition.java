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
package org.helyx.app.j2me.getabike.lib.ui.view.transition;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.IViewTransition;
import org.helyx.logging4me.Logger;


public class BasicTransition implements IViewTransition {

	private static final Logger logger = Logger.getLogger("BASIC_TRANSITION");
	
	public BasicTransition() {
		super();
	}

	public void doTransition(Graphics graphics, AbstractView srcView, AbstractView targetView) {
		GameCanvas canvas = srcView.getViewCanvas();
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		Image destImage = Image.createImage(width, height);
		
		targetView.onPaint(destImage.getGraphics());

		graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
		canvas.flushGraphics();
	}

}
