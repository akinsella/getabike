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
package org.helyx.app.j2me.getabike.lib.ui.view.support.menu;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.renderer.text.ITextRenderer;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuElementProvider;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class MenuListView extends AbstractListView {

	private static final Logger logger = Logger.getLogger("MENU_LIST_VIEW");
	
	private static final int PADDING = 5;

	private boolean checkable = false;
	
	private ITextRenderer itemTextRenderer;

	public MenuListView(AbstractMIDlet midlet, String title, boolean checkable) {
		super(midlet, title);
		this.checkable = checkable;
		init();
	}
	
	private void init() {
		itemTextRenderer = getMidlet().getI18NTextRenderer();
		initActions();
	}
	
	protected void initActions() {

		if (logger.isDebugEnabled()) {
			logger.debug("Menu List View Checkable: " + checkable);
		}
		if (checkable) {
			setPrimaryCommand(new Command("command.select", true, getMidlet().getI18NTextRenderer(), new IAction() {

				public void run(Object data) {
					MenuItem menuItem = getMenu().getSelectedMenuItem();
					if (menuItem != null) {
						getMenu().setCheckedMenuItem(menuItem);
						MenuListView.this.getViewCanvas().repaint();
					}
				}
				
			}));
		}
		
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));

	}

	protected void initComponents() {
		// TODO Auto-generated method stub
		
	}

	protected void initData() {
		
	}

	public Menu getMenu() {
		return ((MenuElementProvider)getElementProvider()).getMenu();
	}

	public void setMenu(Menu menu) {
		menu.setSelectedMenuItemIndex(0);
		setElementProvider(new MenuElementProvider(menu));
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
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
        int width = PADDING;
        g.setFont(FontUtil.MEDIUM);
        
        if (checkable) {
             if (getMenu().getCheckedMenuItem() == menuItem) {
            	g.fillArc(itemClientArea.location.x + width, itemClientArea.location.y + (itemClientArea.size.height - PADDING) / 2, 5, 5, 0, 360);
            }
            	width += 10 + PADDING;
       }

        ImageSet imageSet = menuItem.getImageSet();
    	if (imageSet != null) {
    		Image image = null;
    		
    		if (menuItem.isEnabled()) {
        		if (isSelected) {
        			image = imageSet.getActiveImage() != null ? imageSet.getActiveImage() : imageSet.getImage();
        		}
        		else {
        			image = imageSet.getImage();
        		}
    		}
    		else {
    			image = imageSet.getDisabledImage() != null ? imageSet.getDisabledImage() : imageSet.getImage();
    		}
    		
    		if (image != null) {
                g.drawImage(image, itemClientArea.location.x + width, itemClientArea.location.y + (itemClientArea.size.height - image.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);     	
    			width += image.getWidth() + PADDING;
    		}
    	}

        g.drawString(renderItemText(menuItem), itemClientArea.location.x + width, itemClientArea.location.y + (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
 	}
	
	public void setItemTextRenderer(ITextRenderer itemTextRenderer) {
		this.itemTextRenderer = itemTextRenderer;
	}
	
	private String renderItemText(MenuItem menuItem) {
		if (menuItem == null) {
			return null;
		}
		return itemTextRenderer != null ? itemTextRenderer.renderText(menuItem.getText()) : menuItem.getText();
	}

	protected void onShowItemSelected(Object object) {
		if (checkable) {
			return;
		}
		executeSelectedMenuItemAction();
	}
	
	protected void selectPreviousItem() {
		super.selectPreviousItem();
		updateMenuSelected();
	}

	protected void selectNextItem() {
		super.selectNextItem();
		updateMenuSelected();
	}

	private void executeSelectedMenuItemAction() {
		MenuItem menuItem = getMenu().getSelectedMenuItem();
		if (logger.isDebugEnabled()) {
			logger.debug("menuItem: " + menuItem.getText());
		}
		if (menuItem.getAction() != null) {
			menuItem.getAction().run(null);
		}
	}

	private void updateMenuSelected() {
		getMenu().setSelectedMenuItemIndex(selectedOffset);
	}

	
}
