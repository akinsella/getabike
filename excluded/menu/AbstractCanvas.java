package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.AbstractMIDlet;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.geometry.Point;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.lib.ui.widget.ColorUtil;
import org.helyx.app.j2me.lib.ui.widget.FontUtil;
import org.helyx.app.j2me.lib.ui.widget.action.ActionItem;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuWidget;

public abstract class AbstractCanvas extends AbstractDisplayable {

	private static final String CAT = "ABSTRACT_CANVAS";
	
	
	protected String title;
	protected boolean isFullScreenMode = false;
	protected Menu menu;
	
	protected ActionItem primaryAction;
	protected ActionItem secondaryAction;
	
	protected boolean titleEnabled = true;
	protected boolean menuEnabled = true;
	
	protected static AbstractGameCanvas canvas = new AbstractGameCanvas();
	
	protected int backgroundColor = ColorUtil.WHITE;
	protected boolean paintBackgroundColor = true;
	private MenuWidget menuWidget;
	private boolean menuShowed = false;

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
	    if (menuShowed) {
	    	menuWidget.onKeyReleased(keyCode);
	    }
	    else {
	    	onKeyReleased(keyCode);
	    }
	}

	protected void keyPressed(int keyCode) {

	    if (menuShowed) {
	    	menuWidget.onKeyReleased(keyCode);
	    }
	    else {

			if (!checkMenuEvent(keyCode)) {
			    onKeyPressed(keyCode);
			}
	    }
	}

	private boolean checkMenuEvent(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.debug(CAT, keyCode + " - " + canvas.getKeyName(keyCode));
		
		if (menuEnabled) {
		    if (gameAction == GameCanvas.FIRE && primaryAction != null && primaryAction.isEnabled()) {
				primaryAction.getAction().run();
		    	return true;
			}
		    else if ((gameAction == GameCanvas.GAME_B || keyCode == KeyUtil.SOFT_2) && secondaryAction != null && secondaryAction.isEnabled()) {
		    	secondaryAction.getAction().run();
		    	return true;
			}
		    else if ((gameAction == GameCanvas.GAME_A || keyCode == KeyUtil.SOFT_1) && menu != null && menu.isEnabled()) {
		    	canvas.showMenu();
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

	protected void keyRepeated(int keyCode) {
		if (menuShowed) {
		}
		else {
			onKeyRepeated(keyCode);
		}
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
		return isFullScreenMode;
	}
	
	public void setFullScreenMode(boolean isFullScreenMode) {
		this.isFullScreenMode = isFullScreenMode;
		canvas.setFullScreenMode(isFullScreenMode);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Menu getMenu() {
		return menu;
	}

	public ActionItem getPrimaryAction() {
		return primaryAction;
	}

	public ActionItem getSecondaryAction() {
		return secondaryAction;
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
		Log.debug(CAT, "onPaint");
		if (menuShowed) {
			menuWidget.paint();
		}
		else {
			paintBackgroundColor(graphics);
			paint(graphics);
			paintTitleArea(graphics);
			paintMenuArea(graphics);
			if (canvas.isDebug()) {
				paintDebug(graphics);
			}
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
		return menuEnabled && (menu != null || primaryAction != null || secondaryAction != null);
	}
	
	protected boolean shouldPaintTitle() {
		return titleEnabled && title != null;
	}
	
	public AbstractGameCanvas getAbstractGameCanvas() {
		return canvas;
	}
	
	protected void changeDisplayable(AbstractDisplayable targetDisplayable, boolean doTransition, boolean reverse) {

		if (targetDisplayable instanceof AbstractCanvas) {
			Log.debug("targetDisplayable: [" + targetDisplayable.getClass().getName() + "] title='" + targetDisplayable.getTitle() + "'");
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
		Log.debug(CAT, "currentDisplayable: [" + getClass().getName() + "] title=" + currentDisplayable);
		if (currentDisplayable == null || currentDisplayable != targetDisplayable.getDisplayable()) {
			Log.debug(CAT, "targetDisplayable: [" + targetDisplayable.getClass().getName() + "] title=" + targetDisplayable.getTitle());
			Display.getDisplay(getMidlet()).setCurrent(targetDisplayable.getDisplayable());
		}
	}

	private void paintBackgroundColor(Graphics g) {
		if (!paintBackgroundColor) {
			return;
		}
        g.setColor(ColorUtil.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	private void paintTitleArea(Graphics graphics) {
		if (!shouldPaintTitle()) {
			return;
		}

		Rectangle titleArea = computeTitleArea(graphics);
		graphics.setColor(ColorUtil.WHITE);
		GraphicsUtil.fillShade(graphics, titleArea, new Shade(ColorUtil.WIDGET_TITLE_BG_SHADE_LIGHT, ColorUtil.WIDGET_TITLE_BG_SHADE_DARK), false);

		graphics.setColor(ColorUtil.WIDGET_TITLE_FONT);
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(titleArea.location.x, titleArea.location.y + titleArea.size.height, titleArea.location.x + titleArea.size.width, titleArea.location.y + titleArea.size.height);
		graphics.drawString(title, titleArea.location.x + titleArea.size.width / 2, titleArea.location.y + (titleArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
	}

	private void paintMenuArea(Graphics graphics) {
		if (!shouldPaintMenu()) {
			return;
		}

		Rectangle menuArea = computeMenuArea(graphics);
		graphics.setColor(ColorUtil.WHITE);
		GraphicsUtil.fillShade(graphics, menuArea, new Shade(ColorUtil.WIDGET_TITLE_BG_SHADE_DARK, ColorUtil.WIDGET_TITLE_BG_SHADE_LIGHT), false);

		graphics.setColor(ColorUtil.WIDGET_TITLE_FONT);
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(menuArea.location.x, menuArea.location.y, menuArea.location.x + menuArea.size.width, menuArea.location.y);

		if (menu != null) {
			graphics.drawString("Menu", menuArea.location.x, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.LEFT | Graphics.TOP);
		}

		if (primaryAction != null) {
			graphics.drawString(primaryAction.getText(), menuArea.location.x + menuArea.size.width / 2, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (secondaryAction != null) {
			graphics.drawString(secondaryAction.getText(), menuArea.location.x + menuArea.size.width, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.RIGHT | Graphics.TOP);
		}
		
	}
	
	private void showMenu(AbstractCanvas abstractCanvas, Menu menu, Point location, int graphicsMode) {
		menuWidget = new MenuWidget(abstractCanvas, menu, location, graphicsMode);
		menuWidget.show();
	}

	protected abstract void paint(Graphics graphics);
	
	public static final class AbstractGameCanvas extends GameCanvas {

		private boolean keyEventEnabled = true;

		protected boolean debug = true;
		protected int lastKeyCode = 0;
		
		protected AbstractCanvas abstractCanvas;

		protected AbstractGameCanvas() {
			super(false);
		}
		
		public Graphics getGraphics() {
			return super.getGraphics();
		}
		
		public void showMenu() {
			Graphics graphics = getGraphics();
	    	Rectangle clientArea = abstractCanvas.computeClientArea(graphics);
	    	Point location = new Point(clientArea.location.x + clientArea.size.width, clientArea.location.y + clientArea.size.height);
	    	abstractCanvas.showMenu(abstractCanvas, abstractCanvas.menu, location, Graphics.LEFT | Graphics.BOTTOM);
		}

		public void setAbstractCanvas(AbstractCanvas abstractCanvas) {
			Log.debug("Current canvas: " + this.abstractCanvas);
			Log.debug("Target canvas: " + abstractCanvas);
			this.abstractCanvas = abstractCanvas;
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
				
						Log.debug(CAT, "left: " + doTransitionCounter);

						graphics.drawImage(srcImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						graphics.drawImage(destImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						flushGraphics();

						try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

						if (doTransitionCounter - increment <= 0) {
							graphics.drawImage(destImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
							flushGraphics();
							break;
						}
					}
				}
				else {
					while (true) {
						doTransitionCounter += increment;
				
						Log.debug(CAT, "left: " + doTransitionCounter);

						graphics.drawImage(srcImage, 0 - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
						graphics.drawImage(destImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);

						flushGraphics();

						try { Thread.sleep(10); } catch (InterruptedException e) { Log.warn(CAT, e); }

						if (doTransitionCounter + increment >= width) {
							graphics.drawImage(destImage, width - doTransitionCounter, 0, Graphics.TOP | Graphics.LEFT);
							flushGraphics();
							break;
						}
					}
				}
				
				Log.debug("Time ellapsed: " + Math.abs(System.currentTimeMillis() - start));
			}
			finally {
				keyEventEnabled = true;
			}
		}
	}

	public void onMenuEntered() {
		Log.debug(CAT, "onMenuEntered");
		menuShowed = true;
		getAbstractGameCanvas().repaint();
	};

	public void onMenuExited() {
		Log.debug(CAT, "onMenuExited");
		menuShowed = false;
		getAbstractGameCanvas().repaint();
	};
	
}
