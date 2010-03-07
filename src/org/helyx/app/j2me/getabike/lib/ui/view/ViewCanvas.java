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
package org.helyx.app.j2me.getabike.lib.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.IViewTransition;
import org.helyx.logging4me.Logger;



public class ViewCanvas extends GameCanvas {

	private static final Logger logger = Logger.getLogger("VIEW_CANVAS");
	
	private boolean keyEventEnabled = true;
	private boolean pointerEventEnabled = true;

	protected boolean debug = false;
	protected int lastKeyCode = 0;
	
	protected AbstractView view;

	protected ViewCanvas() {
		super(false);
	}
	
	public Graphics getGraphics() {
		return super.getGraphics();
	}

	public void setView(AbstractView view) {
		if (logger.isDebugEnabled()) {
			logger.debug("Current canvas: " + this.view);
			logger.debug("New canvas: " + view);
		}
		this.view = view;
		if (view != null) {
			view.getViewCanvas().repaint();
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public int getLastKeyCode() {
		return lastKeyCode;
	}
	
	public void paint(Graphics graphics) {
		try {
			if (view != null) {
				view.onPaint(graphics);
			}
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}

	protected void keyPressed(int keyCode) {
		if (keyEventEnabled) {
			if (view != null) {
				view.keyPressed(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyReleased(int keyCode) {
		if (keyEventEnabled) {
			if (view != null) {
				view.keyReleased(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyRepeated(int keyCode) {
		if (keyEventEnabled) {
			if (view != null) {
				view.keyRepeated(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void pointerPressed(int x, int y) {
		if (pointerEventEnabled) {
			if (view != null) {
				view.pointerPressed(x, y);
			}
		}
	}

	protected void pointerDragged(int x, int y) {
		if (pointerEventEnabled) {
			if (view != null) {
				view.pointerDragged(x, y);
			}
		}
	}

	protected void pointerReleased(int x, int y) {
		if (pointerEventEnabled) {
			if (view != null) {
				view.pointerReleased(x, y);
			}
		}
	}

	protected void sizeChanged(int w, int h) {
		if (view != null) {
			view.sizeChanged(w, h);
		}
	}

	public void doCanvasTransition(AbstractView srcView, AbstractView targetView, IViewTransition viewTransition) {
		if (srcView == null) {
			return;
		}
		keyEventEnabled = false;
		pointerEventEnabled = false;
		try {
			viewTransition.doTransition(getGraphics(), srcView, targetView);
		}
		finally {
			keyEventEnabled = true;
			pointerEventEnabled = true;
		}
	}
	
}
