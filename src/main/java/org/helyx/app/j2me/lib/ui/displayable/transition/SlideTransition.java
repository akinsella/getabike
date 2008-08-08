package org.helyx.app.j2me.lib.ui.displayable.transition;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.view.AbstractView;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;

public class SlideTransition implements IViewTransition {

	private static final String CAT = "SLIDE_TRANSITION";
	
	private boolean reverse = false;
	
	public SlideTransition(boolean reverse) {
		super();
		this.reverse = reverse;
	}

	public void doTransition(Graphics graphics, AbstractView srcView, AbstractView targetView) {
		GameCanvas canvas = srcView.getViewCanvas();
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		
		Image srcImage = Image.createImage(width, height);
		Image destImage = Image.createImage(width, height);
		
		if (canvas != null) {
			srcView.onPaint(srcImage.getGraphics());
		}
		else {
			Graphics srcGraphics = srcImage.getGraphics();
			srcGraphics.setColor(ColorUtil.WHITE);
			srcGraphics.fillRect(0, 0, width, height);
		}
		targetView.onPaint(destImage.getGraphics());

		
		long start = System.currentTimeMillis();
		
		int doTransitionCounter = 0;
		int increment = width / 5;
		if (reverse) {
			doTransitionCounter = width;
			increment *= -1;
			
			while (true) {
				doTransitionCounter += increment;

				graphics.drawImage(srcImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
				graphics.drawImage(destImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
				canvas.flushGraphics();

				try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

				if (doTransitionCounter + increment <= 0) {
					graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
					canvas.flushGraphics();
					break;
				}
			}
		}
		else {
			while (true) {
				doTransitionCounter += increment;
				
				graphics.drawImage(srcImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
				graphics.drawImage(destImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);

				canvas.flushGraphics();

				try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

				if (doTransitionCounter + increment >= width) {
					graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
					canvas.flushGraphics();
					break;
				}
			}
		}
		
		Log.debug(CAT, "Screen transition time ellapsed: " + Math.abs(System.currentTimeMillis() - start) + " ms");
	}

}
