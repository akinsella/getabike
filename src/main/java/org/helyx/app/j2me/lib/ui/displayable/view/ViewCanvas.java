package org.helyx.app.j2me.lib.ui.displayable.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.transition.IViewTransition;


public class ViewCanvas extends GameCanvas {

	private static final String CAT = "VIEW_CANVAS";
	
	private boolean keyEventEnabled = true;

	protected boolean debug = false;
	protected int lastKeyCode = 0;
	
	protected AbstractView abstractView;

	protected ViewCanvas() {
		super(false);
	}
	
	public Graphics getGraphics() {
		return super.getGraphics();
	}

	public void setAbstractCanvas(AbstractView abstractView) {
		Log.debug(CAT, "Current canvas: " + this.abstractView);
		Log.debug(CAT, "New canvas: " + abstractView);
		this.abstractView = abstractView;
		if (abstractView != null) {
			abstractView.getViewCanvas().repaint();
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
			if (abstractView != null) {
				abstractView.onPaint(graphics);
			}
		}
		catch(Throwable t) {
			Log.warn(CAT, t);
		}
	}

	protected void keyPressed(int keyCode) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.keyPressed(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyReleased(int keyCode) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.keyReleased(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyRepeated(int keyCode) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.keyRepeated(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void pointerDragged(int x, int y) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.pointerDragged(x, y);
			}
		}
	}

	protected void pointerPressed(int x, int y) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.pointerPressed(x, y);
			}
		}
	}

	protected void pointerReleased(int x, int y) {
		if (keyEventEnabled) {
			if (abstractView != null) {
				abstractView.pointerReleased(x, y);
			}
		}
	}

	protected void sizeChanged(int w, int h) {
		if (abstractView != null) {
			abstractView.sizeChanged(w, h);
		}
	}

	public void doCanvasTransition(AbstractView targetAbstractCanvas, IViewTransition canvasTransition) {
		keyEventEnabled = false;
		try {
		}
		finally {
			keyEventEnabled = true;
		}
	}
}
