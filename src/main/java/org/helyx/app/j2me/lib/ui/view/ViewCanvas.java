package org.helyx.app.j2me.lib.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;


public class ViewCanvas extends GameCanvas {

	private static final Log log = LogFactory.getLog("VIEW_CANVAS");
	
	private boolean keyEventEnabled = true;

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
		log.debug("Current canvas: " + this.view);
		log.debug("New canvas: " + view);
		this.view = view;
		if (view != null) {
			view.getViewCanvas().repaint();
		}
	}

	public boolean isDebug() {
		return debug;
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
			log.warn(t);
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

	protected void pointerDragged(int x, int y) {
		if (keyEventEnabled) {
			if (view != null) {
				view.pointerDragged(x, y);
			}
		}
	}

	protected void pointerPressed(int x, int y) {
		if (keyEventEnabled) {
			if (view != null) {
				view.pointerPressed(x, y);
			}
		}
	}

	protected void pointerReleased(int x, int y) {
		if (keyEventEnabled) {
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
		try {
			viewTransition.doTransition(getGraphics(), srcView, targetView);
		}
		finally {
			keyEventEnabled = true;
		}
	}
	
}
