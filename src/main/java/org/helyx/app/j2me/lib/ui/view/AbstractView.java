package org.helyx.app.j2me.lib.ui.view;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.lib.ui.view.painter.DefaultBackgroundZone;
import org.helyx.app.j2me.lib.ui.view.painter.SmallMenuZone;
import org.helyx.app.j2me.lib.ui.view.painter.SmallTitleZone;
import org.helyx.app.j2me.lib.ui.view.painter.ViewCanvasZone;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;

public abstract class AbstractView extends AbstractDisplayable {

	private static final Logger logger = LoggerFactory.getLogger("ABSTRACT_VIEW");
	
	protected String title;
	protected boolean fullScreenMode = false;
	
	private ViewCanvasZone titleZone = new SmallTitleZone();
	private ViewCanvasZone menuZone = new SmallMenuZone();
	private ViewCanvasZone backgroundZone = new DefaultBackgroundZone();
	
	private Command primaryCommand;
	private Command secondaryCommand;
	private Command thirdCommand;
	
	private boolean primaryCommandEnabled = true;
	private boolean secondaryCommandEnabled = true;
	private boolean thirdCommandEnabled = true;
		
	protected boolean backgroundEnabled = true;
	protected boolean titleEnabled = true;
	protected boolean menuEnabled = true;
	
	protected static ViewCanvas viewCanvas = new ViewCanvas();

	public AbstractView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}

	private void init() {
		
	}

	public ViewCanvasZone getTitleZone() {
		return titleZone;
	}

	public void setTitleZone(ViewCanvasZone titleZone) {
		this.titleZone = titleZone;
	}

	public ViewCanvasZone getMenuZone() {
		return menuZone;
	}

	public void setMenuZone(ViewCanvasZone menuZone) {
		this.menuZone = menuZone;
	}

	public ViewCanvasZone getBackgroundZone() {
		return backgroundZone;
	}

	public void setBackgroundZone(ViewCanvasZone backgroundZone) {
		this.backgroundZone = backgroundZone;
	}

	protected void sizeChanged(int w, int h) {

	}

	public void onPointerPressed(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void onPointerReleased(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void onPointerDragged(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	protected void pointerReleased(int x, int y) {
		onPointerReleased(x, y);
	}

	protected void pointerPressed(int x, int y) {
		onPointerPressed(x, y);
	}

	protected void pointerDragged(int x, int y) {
		onPointerDragged(x, y);
	}

	protected void keyReleased(int keyCode) {
	    onKeyReleased(keyCode);
	}

	protected void keyPressed(int keyCode) {
		logger.debug("Key pressed: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");

		if (!checkMenuEvent(keyCode)) {
			onKeyPressed(keyCode);
		}
	}

	protected void keyRepeated(int keyCode) {
		logger.debug("Key repeated: '" + viewCanvas.getKeyName(keyCode) + "' - Key code: '" + keyCode + "'");
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
	
	public void flushGraphics() {
		viewCanvas.flushGraphics();
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

	public boolean isBackgroundEnabled() {
		return backgroundEnabled;
	}

	public void setBackgroundEnabled(boolean paintBackgroundColor) {
		this.backgroundEnabled = paintBackgroundColor;
	}
	
	protected Rectangle computeClientArea(Graphics graphics) {
		Rectangle titleArea = titleZone.computeArea(this, graphics);
		Rectangle menuArea = menuZone.computeArea(this, graphics);
		return new Rectangle(0, titleArea.size.height, viewCanvas.getWidth(), viewCanvas.getHeight() - titleArea.size.height - menuArea.size.height);
	}
	
	protected void repaint() {
		viewCanvas.repaint();
	}

	public void onPaint(Graphics graphics) {
		logger.debug("Event: Painting screen");

		paintBackground(graphics);
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

	public boolean shouldPaintBackground() {
		return backgroundEnabled;
	}
	
	public boolean shouldPaintMenu() {
		return menuEnabled && (thirdCommand != null || primaryCommand != null || secondaryCommand != null);
	}
	
	public boolean shouldPaintTitle() {
		return titleEnabled && title != null;
	}
	
	public ViewCanvas getViewCanvas() {
		return viewCanvas;
	}
	
	protected void changeDisplayable(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition viewTransition) {
		if (srcDisplayable == null) {
			logger.debug("Current displayable: [" + null + "]");
		}
		else {
			logger.debug("Current displayable: [" + srcDisplayable.getClass().getName() + "] title='" + srcDisplayable.getTitle() + "'");
		}
		logger.debug("Target displayable: [" + targetDisplayable.getClass().getName() + "] title='" + targetDisplayable.getTitle() + "'");

		if (srcDisplayable != null && (srcDisplayable instanceof AbstractView) && (targetDisplayable instanceof AbstractView)) {
			AbstractView srcView = (AbstractView)srcDisplayable;
			AbstractView targetView = (AbstractView)targetDisplayable;

			try { viewCanvas.doCanvasTransition(srcView, targetView, viewTransition); }
			catch(Throwable t) { logger.warn(t); }
		}
		
		
		super.changeDisplayable(srcDisplayable, targetDisplayable, viewTransition);
	}
	
	protected void onSelection() {
			viewCanvas.setView(this);
	}
	
	protected void onLeave() {
			viewCanvas.setView(null);
	}

	private void paintBackground(Graphics graphics) {
		if (!shouldPaintBackground()) {
			return;
		}

		Rectangle backgroundArea = backgroundZone.computeArea(this, graphics);
		backgroundZone.paintArea(this, graphics, backgroundArea);
	}

	private void paintTitleArea(Graphics graphics) {
		if (!shouldPaintTitle()) {
			return;
		}
		
		Rectangle titleArea = titleZone.computeArea(this, graphics);
		titleZone.paintArea(this, graphics, titleArea);
	}

	private void paintMenuArea(Graphics graphics) {
		if (!shouldPaintMenu()) {
			return;
		}

		Rectangle menuArea = menuZone.computeArea(this, graphics);
		menuZone.paintArea(this, graphics, menuArea);
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
			logger.debug("afterDisplayableSelection[previous]: " + previous);
			logger.debug("afterDisplayableSelection[current]: " + current);
			logger.debug("fullScreenMode: " + fullScreenMode);
			viewCanvas.setFullScreenMode(fullScreenMode);
		}
	}

	public boolean isPrimaryCommandEnabled() {
		return primaryCommandEnabled;
	}

	public void setPrimaryCommandEnabled(boolean primaryCommandEnabled) {
		boolean changed = primaryCommandEnabled != this.primaryCommandEnabled;
		this.primaryCommandEnabled = primaryCommandEnabled;
		if (changed) {
			repaint();
		}
	}

	public boolean isSecondaryCommandEnabled() {
		return secondaryCommandEnabled;
	}

	public void setSecondaryCommandEnabled(boolean secondaryCommandEnabled) {
		boolean changed = primaryCommandEnabled != this.primaryCommandEnabled;
		this.secondaryCommandEnabled = secondaryCommandEnabled;
		if (changed) {
			repaint();
		}
	}

	public boolean isThirdCommandEnabled() {
		return thirdCommandEnabled;
	}

	public void setThirdCommandEnabled(boolean thirdCommandEnabled) {
		boolean changed = primaryCommandEnabled != this.primaryCommandEnabled;
		this.thirdCommandEnabled = thirdCommandEnabled;
		if (changed) {
			repaint();
		}
	}

}
