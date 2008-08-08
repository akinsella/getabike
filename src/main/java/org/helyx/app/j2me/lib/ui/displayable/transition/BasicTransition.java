package org.helyx.app.j2me.lib.ui.displayable.transition;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class BasicTransition implements IViewTransition {

	private static final String CAT = "BASIC_TRANSITION";
	
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
