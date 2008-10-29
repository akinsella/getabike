package org.helyx.app.j2me.lib.ui.view.support;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.lib.ui.view.support.list.MenuElementProvider;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;

public class MenuListView extends AbstractListView {

	private static final Log log = LogFactory.getLog("MENU_LIST_VIEW");

	private boolean checkable = false;

	public MenuListView(AbstractMIDlet midlet, String title, boolean checkable) {
		super(midlet, title);
		this.checkable = checkable;
		init();
	}
	
	private void init() {
		initActions();
	}
	
	protected void initActions() {

		log.debug("Menu List View Checkable: " + checkable);
		
		if (checkable) {
			setPrimaryCommand(new Command("Sélectionner", true, new IAction() {

				public void run(Object data) {
					MenuItem menuItem = getMenu().getSelectedMenuItem();
					if (menuItem != null) {
						getMenu().setCheckedMenuItem(menuItem);
						MenuListView.this.getViewCanvas().repaint();
					}
				}
				
			}));
		}
		
		setSecondaryCommand(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));

	}

	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}

	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	public Menu getMenu() {
		return ((MenuElementProvider)getElementProvider()).getMenu();
	}

	public void setMenu(Menu menu) {
		menu.setSelectedMenuItemIndex(0);
		setElementProvider(new MenuElementProvider(menu));
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
		super.paintItem(g, offset, itemClientArea, itemObject);
		
		MenuItem menuItem = (MenuItem)itemObject;
    	
    	boolean isSelected = isItemSelected(offset);

    	if (isSelected) {
    		Color menuListFontSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SELECTED);
    		g.setColor(menuListFontSelectedColor.intValue());
    	}
    	else {
    		Color menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT);
    		g.setColor(menuListFontColor.intValue());
    	}

        g.setFont(FontUtil.MEDIUM_BOLD);
        if (!checkable) {
            g.drawString(menuItem.getText(), itemClientArea.location.x + 5, itemClientArea.location.y +  (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
        }
        else {
        	int CHECKABLE_WIDTH = 10;
            if (getMenu().getCheckedMenuItem() == menuItem) {
            	g.fillArc(itemClientArea.location.x + 5, itemClientArea.location.y + (itemClientArea.size.height - 5) / 2, 5, 5, 0, 360);
            }
            g.drawString(menuItem.getText(), itemClientArea.location.x + 5 + CHECKABLE_WIDTH, itemClientArea.location.y + (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
        }
 	}

	protected void onShowItemSelected(Object object) {
		if (checkable) {
			return;
		}
		executeSelectedMenuItemAction();
	}

	private void executeSelectedMenuItemAction() {
		MenuItem menuItem = getMenu().getSelectedMenuItem();
		log.debug("menuItem: " + menuItem);
		if (menuItem.getAction() != null) {
			menuItem.getAction().run(null);
		}
	}

	protected void onScrollDown() {
		updateMenuSelected();
	}

	protected void onScrollUp() {
		updateMenuSelected();
	}
	
	private void updateMenuSelected() {
		getMenu().setSelectedMenuItemIndex(selectedOffset);
	}

	
}
