package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.lib.ui.widget.Command;

public abstract class AbstractCanvas extends AbstractDisplayable {

	private static final String CAT = "ABSTRACT_CANVAS";
	
	protected String title;
	protected boolean fullScreenMode = false;
	
	private Command primaryAction;
	private Command secondaryAction;
	private Command thirdAction;
	
	protected boolean titleEnabled = true;
	protected boolean menuEnabled = true;
	
	protected static AbstractGameCanvas canvas = new AbstractGameCanvas();
	
	protected boolean paintBackgroundColor = true;

	public AbstractCanvas(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}

	private void init() {
	}

	protected void sizeChanged(int w, int h) {

	}

	protected void pointerReleased(int x, int y) {

	}

	protected void pointerPressed(int x, int y) {
	
	}

	protected void pointerDragged(int x, int y) {
	
	}

	protected void keyReleased(int keyCode) {
	    onKeyReleased(keyCode);
	}

	protected void keyPressed(int keyCode) {
		Log.debug(CAT, "Key pressed: '" + canvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");

		if (!checkMenuEvent(keyCode)) {
			onKeyPressed(keyCode);
		}
	}

	protected void keyRepeated(int keyCode) {
		Log.debug(CAT, "Key repeated: '" + canvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");
		onKeyRepeated(keyCode);
	}

	private boolean checkMenuEvent(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		
		if (menuEnabled) {
		    if (gameAction == GameCanvas.FIRE && primaryAction != null && primaryAction.isEnabled()) {
				primaryAction.getAction().run(null);
		    	return true;
			}
		    else if (KeyUtil.isRightSoftKey(keyCode) && secondaryAction != null && secondaryAction.isEnabled()) {
		    	secondaryAction.getAction().run(null);
		    	return true;
			}
		    else if (KeyUtil.isLeftSoftKey(keyCode) && thirdAction != null && thirdAction.isEnabled()) {
		    	thirdAction.getAction().run(null);
		    	return true;
		    }
		}
		
		return false;
	}

	protected void onKeyReleased(int keyCode) {
		
	}

	protected void onKeyPressed(int keyCode) {
		
	}

	protected void onKeyRepeated(int keyCode) {
		
	}

	public Displayable getDisplayable() {
		return canvas;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isFullScreenMode() {
		return fullScreenMode;
	}
	
	public void setFullScreenMode(boolean fullScreenMode) {
		this.fullScreenMode = fullScreenMode;
		canvas.setFullScreenMode(fullScreenMode);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Command getPrimaryAction() {
		return primaryAction;
	}

	public Command getSecondaryAction() {
		return secondaryAction;
	}

	public Command getThirdAction() {
		return thirdAction;
	}
	
	public boolean isTitleEnabled() {
		return titleEnabled;
	}

	public void setTitleEnabled(boolean titleEnabled) {
		this.titleEnabled = titleEnabled;
	}

	public boolean isMenuEnabled() {
		return menuEnabled;
	}

	public void setMenuEnabled(boolean menuEnabled) {
		this.menuEnabled = menuEnabled;
	}

	public boolean isPaintBackgroundColor() {
		return paintBackgroundColor;
	}

	public void setPaintBackgroundColor(boolean paintBackgroundColor) {
		this.paintBackgroundColor = paintBackgroundColor;
	}
	
	protected Rectangle computeClientArea(Graphics graphics) {
		
		Rectangle titleArea = computeTitleArea(graphics);
		Rectangle menuArea = computeMenuArea(graphics);
		return new Rectangle(0, titleArea.size.height, canvas.getWidth(), canvas.getHeight() - titleArea.size.height - menuArea.size.height);
	}
	
	protected Rectangle computeMenuArea(Graphics graphics) {
		return new Rectangle(0, canvas.getHeight() - (shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) - 1, canvas.getWidth(), (shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) + 1);
	}

	protected Rectangle computeTitleArea(Graphics graphics) {
		return new Rectangle(0, 0, canvas.getWidth(), shouldPaintTitle() ? FontUtil.SMALL_BOLD.getHeight() : 0);
	}

	public void onPaint(Graphics graphics) {
		Log.debug(CAT, "Event: Painting screen");

		paintBackgroundColor(graphics);
		paint(graphics);
		paintTitleArea(graphics);
		paintMenuArea(graphics);
		if (canvas.isDebug()) {
			paintDebug(graphics);
		}
	}

	private void paintDebug(Graphics graphics) {
		int lastKeyCode = canvas.getLastKeyCode();
		if (canvas != null && canvas.getLastKeyCode() != 0) {
			graphics.setColor(ColorUtil.BLACK);
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString("Code: " + lastKeyCode + ", Name: " + canvas.getKeyName(lastKeyCode) + ", : " + canvas.getGameAction(lastKeyCode), 0, 0, Graphics.TOP | Graphics.LEFT);
		}
	}

	private boolean shouldPaintMenu() {
		return menuEnabled && (thirdAction != null || primaryAction != null || secondaryAction != null);
	}
	
	protected boolean shouldPaintTitle() {
		return titleEnabled && title != null;
	}
	
	public AbstractGameCanvas getAbstractGameCanvas() {
		return canvas;
	}
	
	protected void changeDisplayable(IAbstractDisplayable targetDisplayable, boolean doTransition, boolean reverse) {
		Log.debug(CAT, "Current displayable: [" + getClass().getName() + "] title='" + getTitle() + "'");
		Log.debug(CAT, "Target displayable: [" + targetDisplayable.getClass().getName() + "] title='" + targetDisplayable.getTitle() + "'");

		if (targetDisplayable instanceof AbstractCanvas) {
			AbstractCanvas abstractCanvas = (AbstractCanvas)targetDisplayable;

			if (doTransition) {
				canvas.doCanvasTransition(abstractCanvas, reverse);
			}
			canvas.setAbstractCanvas(abstractCanvas);
		} 
		else {
			canvas.setAbstractCanvas(null);
		}
		
		Displayable currentDisplayable = Display.getDisplay(getMidlet()).getCurrent();
		if (currentDisplayable == null || currentDisplayable != targetDisplayable.getDisplayable()) {
			Display.getDisplay(getMidlet()).setCurrent(targetDisplayable.getDisplayable());
		}
	}

	private void paintBackgroundColor(Graphics g) {
		if (!paintBackgroundColor) {
			return;
		}
		Color bgColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND);

        g.setColor(bgColor.intValue());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void paintTitleArea(Graphics graphics) {
		if (!shouldPaintTitle()) {
			return;
		}

		Rectangle titleArea = computeTitleArea(graphics);
		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		GraphicsUtil.fillShade(graphics, titleArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(titleArea.location.x, titleArea.location.y + titleArea.size.height, titleArea.location.x + titleArea.size.width, titleArea.location.y + titleArea.size.height);
		graphics.drawString(title, titleArea.location.x + titleArea.size.width / 2, titleArea.location.y + (titleArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
	}

	private void paintMenuArea(Graphics graphics) {
		if (!shouldPaintMenu()) {
			return;
		}

		Rectangle menuArea = computeMenuArea(graphics);
		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		GraphicsUtil.fillShade(graphics, menuArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(menuArea.location.x, menuArea.location.y, menuArea.location.x + menuArea.size.width, menuArea.location.y);

		if (thirdAction != null) {
			graphics.drawString(thirdAction.getText(), menuArea.location.x, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.LEFT | Graphics.TOP);
		}

		if (primaryAction != null) {
			graphics.drawString(primaryAction.getText(), menuArea.location.x + menuArea.size.width / 2, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (secondaryAction != null) {
			graphics.drawString(secondaryAction.getText(), menuArea.location.x + menuArea.size.width, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.RIGHT | Graphics.TOP);
		}
		
	}

	protected abstract void paint(Graphics graphics);
	
	public void setPrimaryAction(Command primaryAction) {
		this.primaryAction = primaryAction;
	}

	public void setSecondaryAction(Command secondaryAction) {
		this.secondaryAction = secondaryAction;
	}

	public void setThirdAction(Command thirdAction) {
		this.thirdAction = thirdAction;
	}

	public static final class AbstractGameCanvas extends GameCanvas {

		private boolean keyEventEnabled = true;

		protected boolean debug = false;
		protected int lastKeyCode = 0;
		
		protected AbstractCanvas abstractCanvas;

		protected AbstractGameCanvas() {
			super(false);
		}
		
		public Graphics getGraphics() {
			return super.getGraphics();
		}

		public void setAbstractCanvas(AbstractCanvas abstractCanvas) {
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

		public void doCanvasTransition(AbstractCanvas targetAbstractCanvas, boolean goBack) {
			keyEventEnabled = false;
			try {
				Graphics graphics = getGraphics();
				int width = getWidth();
				int height = getHeight();

				
				Image srcImage = Image.createImage(width, height);
				Image destImage = Image.createImage(width, height);
				
				if (abstractCanvas != null) {
					abstractCanvas.onPaint(srcImage.getGraphics());
				}
				else {
					Graphics srcGraphics = srcImage.getGraphics();
					srcGraphics.setColor(ColorUtil.WHITE);
					srcGraphics.fillRect(0, 0, getWidth(), getHeight());
				}
				targetAbstractCanvas.onPaint(destImage.getGraphics());

				
				long start = System.currentTimeMillis();
				
				int doTransitionCounter = 0;
				int increment = width / 5;
				if (goBack) {
					doTransitionCounter = width;
					increment *= -1;
					
					while (true) {
						doTransitionCounter += increment;

						graphics.drawImage(srcImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						graphics.drawImage(destImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						flushGraphics();

						try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

						if (doTransitionCounter + increment <= 0) {
							graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
							flushGraphics();
							break;
						}
					}
				}
				else {
					while (true) {
						doTransitionCounter += increment;
						
						graphics.drawImage(srcImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						graphics.drawImage(destImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);

						flushGraphics();

						try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

						if (doTransitionCounter + increment >= width) {
							graphics.drawImage(destImage, 0, 0, Graphics.TOP | Graphics.LEFT);
							flushGraphics();
							break;
						}
					}
				}
				
				Log.debug(CAT, "Screen transition time ellapsed: " + Math.abs(System.currentTimeMillis() - start) + " ms");
			}
			finally {
				keyEventEnabled = true;
			}
		}
	}

}
