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
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.IViewTransition;
import org.helyx.logging4me.Logger;


public class SlideTransition implements IViewTransition {

	private static final Logger logger = Logger.getLogger("SLIDE_TRANSITION");
	
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

				try { Thread.sleep(10); } catch (InterruptedException e) { logger.warn(e); }

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

				try { Thread.sleep(10); } catch (InterruptedException e) { logger.warn(e); }

				if (doTransitionCounter + increment >= width) {
					graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
					canvas.flushGraphics();
					break;
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Screen transition time ellapsed: " + Math.abs(System.currentTimeMillis() - start) + " ms");
		}
	}

}
