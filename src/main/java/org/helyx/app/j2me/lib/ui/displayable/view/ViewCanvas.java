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
	
	protected AbstractView abstractCanvas;

	protected ViewCanvas() {
		super(false);
	}
	
	public Graphics getGraphics() {
		return super.getGraphics();
	}

	public void setAbstractCanvas(AbstractView abstractCanvas) {
		Log.debug(CAT, "Current canvas: " + this.abstractCanvas);
		Log.debug(CAT, "New canvas: " + abstractCanvas);
		this.abstractCanvas = abstractCanvas;
		if (abstractCanvas != null) {
			abstractCanvas.getAbstractGameCanvas().repaint();
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
			if (abstractCanvas != null) {
				abstractCanvas.onPaint(graphics);
			}
		}
		catch(Throwable t) {
			Log.warn(CAT, t);
		}
	}

	protected void keyPressed(int keyCode) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.keyPressed(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyReleased(int keyCode) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.keyReleased(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void keyRepeated(int keyCode) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.keyRepeated(keyCode);
			}
			lastKeyCode = keyCode;
			if (debug) {
				repaint();
			}
		}
	}

	protected void pointerDragged(int x, int y) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.pointerDragged(x, y);
			}
		}
	}

	protected void pointerPressed(int x, int y) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.pointerPressed(x, y);
			}
		}
	}

	protected void pointerReleased(int x, int y) {
		if (keyEventEnabled) {
			if (abstractCanvas != null) {
				abstractCanvas.pointerReleased(x, y);
			}
		}
	}

	protected void sizeChanged(int w, int h) {
		if (abstractCanvas != null) {
			if (abstractCanvas != null) {
				abstractCanvas.sizeChanged(w, h);
			}
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
