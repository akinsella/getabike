package org.helyx.app.j2me.lib.ui.view;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;

public abstract class AbstractView extends AbstractDisplayable {

	private static final String CAT = "ABSTRACT_VIEW";
	
	protected String title;
	protected boolean fullScreenMode = false;
	
	private Command primaryCommand;
	private Command secondaryCommand;
	private Command thirdCommand;
	
	protected boolean titleEnabled = true;
	protected boolean menuEnabled = true;
	
	protected static ViewCanvas viewCanvas = new ViewCanvas();
	
	protected boolean paintBackgroundColor = true;

	public AbstractView(AbstractMIDlet midlet) {
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
		Log.debug(CAT, "Key pressed: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");

		if (!checkMenuEvent(keyCode)) {
			onKeyPressed(keyCode);
		}
	}

	protected void keyRepeated(int keyCode) {
		Log.debug(CAT, "Key repeated: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");
		onKeyRepeated(keyCode);
	}

	private boolean checkMenuEvent(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		
		if (menuEnabled) {
		    if (gameAction == GameCanvas.FIRE && primaryCommand != null && primaryCommand.isEnabled()) {
				primaryCommand.getAction().run(null);
		    	return true;
			}
		    else if (KeyUtil.isRightSoftKey(keyCode) && secondaryCommand != null && secondaryCommand.isEnabled()) {
		    	secondaryCommand.getAction().run(null);
		    	return true;
			}
		    else if (KeyUtil.isLeftSoftKey(keyCode) && thirdCommand != null && thirdCommand.isEnabled()) {
		    	thirdCommand.getAction().run(null);
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
		return viewCanvas;
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
		viewCanvas.setFullScreenMode(fullScreenMode);
	}

	public Command getPrimaryCommand() {
		return primaryCommand;
	}

	public Command getSecondaryCommand() {
		return secondaryCommand;
	}

	public Command getThirdCommand() {
		return thirdCommand;
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
		return new Rectangle(0, titleArea.size.height, viewCanvas.getWidth(), viewCanvas.getHeight() - titleArea.size.height - menuArea.size.height);
	}
	
	protected Rectangle computeMenuArea(Graphics graphics) {
		return new Rectangle(0, viewCanvas.getHeight() - (shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) - 1, viewCanvas.getWidth(), (shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) + 1);
	}

	protected Rectangle computeTitleArea(Graphics graphics) {
		return new Rectangle(0, 0, viewCanvas.getWidth(), shouldPaintTitle() ? FontUtil.SMALL_BOLD.getHeight() : 0);
	}
	
	protected void repaint() {
		viewCanvas.repaint();
	}

	public void onPaint(Graphics graphics) {
		Log.debug(CAT, "Event: Painting screen");

		paintBackgroundColor(graphics);
		paint(graphics);
		paintTitleArea(graphics);
		paintMenuArea(graphics);
		if (viewCanvas.isDebug()) {
			paintDebug(graphics);
		}
	}

	private void paintDebug(Graphics graphics) {
		int lastKeyCode = viewCanvas.getLastKeyCode();
		if (viewCanvas != null && viewCanvas.getLastKeyCode() != 0) {
			graphics.setColor(ColorUtil.BLACK);
			graphics.setFont(FontUtil.SMALL_BOLD);
			graphics.drawString("Code: " + lastKeyCode + ", Name: " + viewCanvas.getKeyName(lastKeyCode) + ", : " + viewCanvas.getGameAction(lastKeyCode), 0, 0, Graphics.TOP | Graphics.LEFT);
		}
	}

	private boolean shouldPaintMenu() {
		return menuEnabled && (thirdCommand != null || primaryCommand != null || secondaryCommand != null);
	}
	
	protected boolean shouldPaintTitle() {
		return titleEnabled && title != null;
	}
	
	public ViewCanvas getViewCanvas() {
		return viewCanvas;
	}
	
	protected void changeDisplayable(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition viewTransition) {
		if (srcDisplayable == null) {
			Log.debug(CAT, "Current displayable: [" + null + "]");
		}
		else {
			Log.debug(CAT, "Current displayable: [" + srcDisplayable.getClass().getName() + "] title='" + srcDisplayable.getTitle() + "'");
		}
		Log.debug(CAT, "Target displayable: [" + targetDisplayable.getClass().getName() + "] title='" + targetDisplayable.getTitle() + "'");

		if (srcDisplayable != null && (srcDisplayable instanceof AbstractView) && (targetDisplayable instanceof AbstractView)) {
			AbstractView srcView = (AbstractView)srcDisplayable;
			AbstractView targetView = (AbstractView)targetDisplayable;

			try { viewCanvas.doCanvasTransition(srcView, targetView, viewTransition); }
			catch(Throwable t) { Log.warn(CAT, t); }
		}
		
		if (targetDisplayable instanceof AbstractView) {
			AbstractView targetView = (AbstractView)targetDisplayable;
			viewCanvas.setView(targetView);
		} 
		else {
			viewCanvas.setView(null);
		}
		
		super.changeDisplayable(srcDisplayable, targetDisplayable, viewTransition);
	}

	private void paintBackgroundColor(Graphics g) {
		if (!paintBackgroundColor) {
			return;
		}
		Color bgColor = getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND);

        g.setColor(bgColor.intValue());
        g.fillRect(0, 0, viewCanvas.getWidth(), viewCanvas.getHeight());
	}

	private void paintTitleArea(Graphics graphics) {
		if (!shouldPaintTitle()) {
			return;
		}

		Rectangle titleArea = computeTitleArea(graphics);
		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		GraphicsUtil.fillShade(graphics, titleArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
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
		Color shadeColor1 = getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		Color shadeColor2 = getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		GraphicsUtil.fillShade(graphics, menuArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(menuArea.location.x, menuArea.location.y, menuArea.location.x + menuArea.size.width, menuArea.location.y);

		if (thirdCommand != null) {
			graphics.drawString(thirdCommand.getText(), menuArea.location.x, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.LEFT | Graphics.TOP);
		}

		if (primaryCommand != null) {
			graphics.drawString(primaryCommand.getText(), menuArea.location.x + menuArea.size.width / 2, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (secondaryCommand != null) {
			graphics.drawString(secondaryCommand.getText(), menuArea.location.x + menuArea.size.width, menuArea.location.y + (menuArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.RIGHT | Graphics.TOP);
		}
		
	}

	protected abstract void paint(Graphics graphics);
	
	public void setPrimaryCommand(Command primaryCommand) {
		this.primaryCommand = primaryCommand;
	}

	public void setSecondaryCommand(Command secondaryCommand) {
		this.secondaryCommand = secondaryCommand;
	}

	public void setThirdCommand(Command thirdCommand) {
		this.thirdCommand = thirdCommand;
	}

	public void afterDisplayableSelection(AbstractDisplayable previous, AbstractDisplayable current) {
		if (current == this) {
			Log.debug("afterDisplayableSelection[previous]: " + previous);
			Log.debug("afterDisplayableSelection[current]: " + current);
			Log.debug("fullScreenMode: " + fullScreenMode);
			viewCanvas.setFullScreenMode(fullScreenMode);
		}
	}

}
