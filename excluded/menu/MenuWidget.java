package org.helyx.app.j2me.lib.ui.widget.menu;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas.AbstractGameCanvas;
import org.helyx.app.j2me.lib.ui.geometry.Point;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.widget.ColorUtil;
import org.helyx.app.j2me.lib.ui.widget.FontUtil;

public class MenuWidget {
	
	private static String CAT = "MENU_WIDGET";
	
	private Menu menu;
	private Point location;
	private int graphicsMode;
	private AbstractCanvas abstractCanvas;
	private Image srcImage;
	private boolean exit = false;
	
	public Menu getMenu() {
		return menu;
	}

	public Point getLocation() {
		return location;
	}

	public MenuWidget(AbstractCanvas abstractCanvas, Menu menu, Point location, int graphicsMode) {
		super();
		this.menu = menu;
		this.location = location;
		this.abstractCanvas = abstractCanvas;
		this.graphicsMode = graphicsMode;
	}

	public void show() {
		if (menu == null || menu.menuElementCount() == 0) {
			return ;
		}
		menu.setSelectedMenuItemIndex(0);
		initGraphics();
		abstractCanvas.onMenuEntered();
	};

	private void initGraphics() {
		Log.info(CAT, "initGraphics");
		AbstractGameCanvas abstractGameCanvas = abstractCanvas.getAbstractGameCanvas();
		srcImage = Image.createImage(abstractGameCanvas.getWidth(), abstractGameCanvas.getHeight());
		Graphics srcGraphics = srcImage.getGraphics();
		abstractCanvas.onPaint(srcGraphics);
		repaint();
	}

	public void paint() {
		Log.info(CAT, "updateGraphics");
		AbstractGameCanvas abstractGameCanvas = abstractCanvas.getAbstractGameCanvas();
		Graphics graphics = abstractGameCanvas.getGraphics();
		graphics.drawImage(srcImage, 0, 0, Graphics.TOP | Graphics.LEFT);
//		graphics.setColor(ColorUtil.BLACK);
//		graphics.fillRect(0, 0, abstractGameCanvas.getWidth(), abstractGameCanvas.getHeight());

		try {
			paintMenu(menu, graphics);
		}
		finally {
			abstractGameCanvas.flushGraphics();
		}
	}
	
	private void paintMenu(IMenu menu, Graphics graphics) {
		Rectangle menuArea = computeMenuArea(menu, graphics);
		graphics.setColor(ColorUtil.WHITE);
		graphics.fillRect(menuArea.location.x, menuArea.location.y, menuArea.location.x + menuArea.size.width, menuArea.location.y + menuArea.size.height);		
		graphics.setColor(ColorUtil.WIDGET_TITLE_BG_SHADE_DARK);
		graphics.drawRect(menuArea.location.x, menuArea.location.y, menuArea.location.x + menuArea.size.width, menuArea.location.y + menuArea.size.height);		

		int menuElementCount = menu.menuElementCount();
		int width = menuArea.location.x;
		int height = menuArea.location.y;
		for (int i = 0 ; i < menuElementCount ; i++) {
			IMenuElement menuElement = menu.getMenuElement(i);
			if (menuElement instanceof IMenuItem) {
				IMenuItem menuItem = (IMenuItem)menuElement;
				graphics.setFont(FontUtil.SMALL_BOLD);
				boolean isCurrentSelected = menuItem == menu.getSelectedMenuItem();
				
				if (isCurrentSelected) {
					Shade shade = new Shade(ColorUtil.WIDGET_TITLE_BG_SHADE_DARK, ColorUtil.WIDGET_TITLE_BG_SHADE_LIGHT);
					Rectangle area = new Rectangle(width, height, menuArea.size.width, FontUtil.SMALL_BOLD.getHeight());
					GraphicsUtil.fillShade(graphics, area, shade, false);
				}
				graphics.setColor(isCurrentSelected ? ColorUtil.WIDGET_LIST_FONT_SELECTED : ColorUtil.WIDGET_LIST_FONT);
				graphics.drawString(menuItem.getTitle(), width, height, Graphics.TOP | Graphics.LEFT);
				height += FontUtil.SMALL_BOLD.getHeight();
			}
		}
	}

	private Rectangle computeMenuArea(IMenu menu, Graphics graphics) {
		Log.info(CAT, "computeMenuArea");
		int menuElementCount = menu.menuElementCount();
		int menuWidth = computeMenuWidth(menu, graphics);
		int menuElementHeight = computeMenuElementHeight(menu, graphics);
		return new Rectangle(10, 100, menuWidth, menuElementHeight * menuElementCount);
	}

	private int computeMenuWidth(IMenu menu, Graphics graphics) {
		int menuElementCount = menu.menuElementCount();
		int maxWidth = 10;
		for (int i = 0 ; i < menuElementCount ; i++) {
			IMenuElement menuElement = menu.getMenuElement(i);
			if (menuElement instanceof IMenuItem) {
				IMenuItem menuItem = (IMenuItem)menuElement;
				maxWidth = Math.max(maxWidth, FontUtil.SMALL_BOLD.stringWidth(menuItem.getTitle()));
			}
		}
		return maxWidth;
	}

	private int computeMenuElementHeight(IMenu menu, Graphics graphics) {
		return FontUtil.SMALL_BOLD.getHeight();
	}

	private void onKeyEvent(int keyCode) {
		Log.info(CAT, "checkKeyState");
		AbstractGameCanvas abstractGameCanvas = abstractCanvas.getAbstractGameCanvas();

		int gameAction = abstractGameCanvas.getGameAction(keyCode);
		Log.info(CAT, "[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.FIRE) {
	    	onFirePressed();
	    }
	    else if (gameAction == GameCanvas.LEFT) {
			onLeftPressed();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			onRightPressed();
		}
	    else if (gameAction == GameCanvas.UP) {
			onUpPressed();
		}
	    else if (gameAction == GameCanvas.DOWN) {
			onDownPressed();
		}
	}

	private void onFirePressed() {
		Log.info(CAT, "FIRE_PRESSED");
		IMenuItem menuItem = menu.getSelectedMenuItem();
		if (menuItem != null && (menuItem instanceof SubMenu)) {	
			SubMenu subMenu = (SubMenu)menuItem;
			IMenuItem subMenuItem = subMenu.getSelectedMenuItem();
			if (subMenuItem != null && (subMenuItem instanceof IMenuActionEnabled)) {
				IMenuActionEnabled menuActionEnabled = (IMenuActionEnabled)subMenuItem;
//				menuActionEnabled.getAction().run();
				abstractCanvas.onMenuExited();
			}
		}
		else if (menuItem != null && (menuItem instanceof IMenuActionEnabled)) {
			IMenuActionEnabled menuActionEnabled = (IMenuActionEnabled)menuItem;
			menuActionEnabled.getAction().run();
			menu.setSelectedMenuItem(null);
			abstractCanvas.onMenuExited();
		}
	}
	
	private void onLeftPressed() {
		Log.info(CAT, "LEFT_PRESSED");
		IMenuItem menuItem = menu.getSelectedMenuItem();
		if (menuItem != null && (menuItem instanceof SubMenu)) {
			SubMenu subMenu = (SubMenu)menuItem;
			subMenu.setSelectedMenuItem(null);
		}
		else {
			abstractCanvas.onMenuExited();
		}
	}

	private void onRightPressed() {
		Log.info(CAT, "RIGHT_PRESSED");
		IMenuItem menuItem = menu.getSelectedMenuItem();
		if (menuItem != null && (menuItem instanceof SubMenu)) {
			SubMenu subMenu = (SubMenu)menuItem;
			if (subMenu.getSelectedMenuItem() != null) {
				// Do Nothing
			}
			else if (subMenu.menuElementCount() > 0) {
				subMenu.setSelectedMenuItem(subMenu.getMenuItem(0));
			}
		}
		else {
			abstractCanvas.onMenuExited();
		}
	}
	

	private void onUpPressed() {
		Log.info(CAT, "UP_PRESSED");
		int menuItemSelectedIndex = menu.getSelectedMenuItemIndex();
		if (menuItemSelectedIndex > 0) {
			menu.setSelectedMenuItemIndex(menuItemSelectedIndex - 1);
		}
	}

	private void onDownPressed() {
		Log.info(CAT, "DOWN_PRESSED");
		int menuItemSelectedIndex = menu.getSelectedMenuItemIndex();
		if (menuItemSelectedIndex + 1 < menu.menuItemCount()) {
			menu.setSelectedMenuItemIndex(menuItemSelectedIndex + 1);
		}
	}

	public void onKeyReleased(int keyCode) {
		onKeyEvent(keyCode);
	}

	public void onKeyPressed(int keyCode) {
		onKeyEvent(keyCode);
	}

	public void onKeyRepeated(int keyCode) {
		onKeyEvent(keyCode);
	}
	
	private void repaint() {
		abstractCanvas.getAbstractGameCanvas().repaint();
	}


}
