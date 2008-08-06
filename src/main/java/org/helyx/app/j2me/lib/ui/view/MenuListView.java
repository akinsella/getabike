package org.helyx.app.j2me.lib.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.displayable.IDisplayableReturnCallback;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;

public class MenuListView extends AbstractCanvas {

	private static final String CAT = "MENU_LIST_VIEW";
	
	private int selectedOffset = 0;
	private int topOffset = 0;
	private int visibleItemCount = 0;
	private boolean checkable = false;
	
	private IDisplayableReturnCallback displayableReturnCallback;
	
	private Menu menu;

	public MenuListView(AbstractMIDlet midlet, boolean checkable) {
		super(midlet);
		this.checkable = checkable;
		init();
	}

	public MenuListView(AbstractMIDlet midlet, boolean checkable, IDisplayableReturnCallback displayableReturnCallback) {
		super(midlet);
		
		this.displayableReturnCallback = displayableReturnCallback;
		this.checkable = checkable;
		init();
	}

	public void returnToPreviousDisplayable() {
		super.returnToPreviousDisplayable();
		if (displayableReturnCallback != null ) {
			displayableReturnCallback.onReturn(null);
		}
	}

	private void init() {
		setTitle("Menu");

		Log.debug(CAT, "Menu List View Checkable: " + checkable);
		
		if (checkable) {
			setPrimaryAction(new Command("Sélectionner", true, new IAction() {

				public void run(Object data) {
					MenuItem menuItem = getMenu().getSelectedMenuItem();
					if (menuItem != null) {
						getMenu().setCheckedMenuItem(menuItem);
						MenuListView.this.getCanvas().repaint();
					}
				}
				
			}));
		}
		
		setSecondaryAction(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
		menu.setSelectedMenuItemIndex(0);
		canvas.repaint();
	}

	protected void paint(Graphics g) {
		int length = getMenu().menuItemCount();
		boolean isEmpty = length == 0;
		
		if (!isEmpty && topOffset > length) {
			topOffset = length - 1;
		}
		if (!isEmpty && selectedOffset > length) {
			selectedOffset = length - 1;
		}
			
		Rectangle clientArea = computeClientArea(g);
		
//		g.setColor(ColorUtil.WHITE);
//		g.fillRect(clientArea.location.x, clientArea.location.y, clientArea.location.x + clientArea.size.width, clientArea.location.y + clientArea.size.height);
        g.setFont(FontUtil.SMALL);
        
        if (isEmpty) {
        	g.drawString("Aucun élément chargé", clientArea.location.x + clientArea.size.width / 2, clientArea.location.y + clientArea.size.height / 2, Graphics.HCENTER | Graphics.BASELINE);
        	return ;
        }
        
        int smallFontHeight = FontUtil.SMALL.getHeight();
        int smallBoldFontHeight = FontUtil.SMALL_BOLD.getHeight();
        
        int itemHeight = 1 + smallFontHeight + 1 + smallBoldFontHeight + 1 + 1;
        
        visibleItemCount = clientArea.size.height / itemHeight;

        for (int i = 0 ; i < visibleItemCount + 1 ; i++) {
        	int y = clientArea.location.y + i * itemHeight;
        	Rectangle itemClientArea = new Rectangle(clientArea.location.x, y, clientArea.size.width, itemHeight);
        	paintItem(g, i, itemClientArea);
        }
        paintScrollBar(g, length, topOffset, selectedOffset, visibleItemCount, clientArea);
	}
	
	private void paintScrollBar(Graphics g, int length, int topOffset, int selectedOffset, int visibleItemCount, Rectangle clientArea) {
		if (length == 0 || length <= visibleItemCount) {
			return;
		}
		Color scrollBackgroundColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BACKGROUND);
		g.setColor(scrollBackgroundColor.intValue());
		g.fillRect(clientArea.location.x + clientArea.size.width - 4, clientArea.location.y, 4, clientArea.size.height);
		
		int yHeight = Math.max(clientArea.size.height / 8, clientArea.size.height * visibleItemCount / length);
		int yPos = clientArea.location.y + (int)((clientArea.size.height - yHeight) * ((double)selectedOffset / ((double)length - 1)));
		
		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_DARK);
		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_LIGHT);
		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
		Rectangle area = new Rectangle(clientArea.location.x + clientArea.size.width - 4, yPos, 4, yHeight);
		GraphicsUtil.fillShade(g, area, shade, true);

		Color scrollBorderColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_BACKGROUND);
		g.setColor(scrollBorderColor.intValue());
		g.drawLine(clientArea.location.x + clientArea.size.width - 5, clientArea.location.y, clientArea.location.x + clientArea.size.width - 5, clientArea.location.y + clientArea.size.height);
	}

	private void paintItem(Graphics g, int offset, Rectangle itemClientArea) {
		if (topOffset + offset >= getMenu().menuItemCount()) {
			return ;
		}
		MenuItem menuItem = getMenu().getMenuItem(topOffset + offset);
    	
    	boolean isSelected = selectedOffset == topOffset + offset;
    	
    	if (isSelected) {
    		Color shadeColor1 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_SELECTED_SHADE_LIGHT);
    		Color shadeColor2 = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_SCROLL_SHADE_LIGHT);
    		Shade shade = new Shade(shadeColor1.intValue(), shadeColor2.intValue());
    		GraphicsUtil.fillShade(g, itemClientArea, shade, false);
     	}
    	else {
    		Color listColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST);
    		g.setColor(listColor.intValue());
    		g.fillRect(itemClientArea.location.x, itemClientArea.location.y, itemClientArea.size.width, itemClientArea.size.height);
    	}
    	
		Color listSeparatorColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_LIST_SEPARATOR);
		g.setColor(listSeparatorColor.intValue());
    	g.drawLine(itemClientArea.location.x, itemClientArea.location.y + itemClientArea.size.height - 1, itemClientArea.location.x + itemClientArea.size.width, itemClientArea.location.y + itemClientArea.size.height - 1);
	
    	if (isSelected) {
    		Color menuListFontSelectedColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SELECTED);
    		g.setColor(menuListFontSelectedColor.intValue());
    	}
    	else {
    		Color menuListFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT);
    		g.setColor(menuListFontColor.intValue());
    	}

        g.setFont(FontUtil.MEDIUM_BOLD);
        if (!checkable) {
            g.drawString(menuItem.getTitle(), itemClientArea.location.x + 5, itemClientArea.location.y +  (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
        }
        else {
        	int CHECKABLE_WIDTH = 10;
            if (getMenu().getCheckedMenuItem() == menuItem) {
            	g.fillArc(itemClientArea.location.x + 5, itemClientArea.location.y + (itemClientArea.size.height - 5) / 2, 5, 5, 0, 360);
            }
            g.drawString(menuItem.getTitle(), itemClientArea.location.x + 5 + CHECKABLE_WIDTH, itemClientArea.location.y + (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
        }
 	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.debug(CAT, "[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	    else if (gameAction == GameCanvas.LEFT) {
			returnToPreviousDisplayable();
		}
	    else if (!checkable && gameAction == GameCanvas.RIGHT) {
	    	executeCurrentMenuItemSelectedAction();
		}
	    else if (!checkable && gameAction == GameCanvas.FIRE) {
	    	executeCurrentMenuItemSelectedAction();
	    }
	}

	protected void onKeyRepeated(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.debug(CAT, "[onKeyRepeated] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.DOWN) {
	    	scrollDown();
	    }
	    else if (gameAction == GameCanvas.UP) {
			scrollUp();
		}
	}

	private void executeCurrentMenuItemSelectedAction() {
		MenuItem menuItem = getMenu().getSelectedMenuItem();
		Log.debug(CAT, "menuItem: " + menuItem);
		if (menuItem.getAction() != null) {
			menuItem.getAction().run(null);
		}
	}

	private void scrollUp() {
		if (selectedOffset > 0) {
			selectedOffset--;
		}
		if (selectedOffset < topOffset) {
			topOffset = selectedOffset;
		}
		getMenu().setSelectedMenuItemIndex(selectedOffset);
		Log.debug(CAT, "Scroll Up - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset);
		canvas.repaint();
	}

	private void scrollDown() {
		int length = getMenu().menuItemCount();
		if (selectedOffset + 1 < length) {
			selectedOffset++;
		}
		if (length <= visibleItemCount) {
			topOffset = 0;
		}
		else {
			if (topOffset >= length - visibleItemCount) {
				topOffset = length - visibleItemCount;
			}
			if (topOffset + visibleItemCount <= selectedOffset) {
				topOffset = selectedOffset - visibleItemCount + 1;
			}	
		}
		getMenu().setSelectedMenuItemIndex(selectedOffset);
		Log.debug(CAT, "Scroll Down - topOffset: " + topOffset + ", selectedOffset: " + selectedOffset + ", visibleItemCount: " + visibleItemCount + ", length: " + length);
		canvas.repaint();
	}

}
