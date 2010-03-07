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
package org.helyx.app.j2me.getabike.lib.test.list;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class MenuView extends MenuListView {
	
		private static final Logger logger = Logger.getLogger("MENU_VIEW");
		
		private Image logoImage;
		private String fallbackLogoImageStr;

		protected int getViewPortTop() {
			return super.getViewPortTop() + super.getViewPortHeight() - getViewPortHeight() - 4;
		}
		
		protected int getViewPortHeight() {
			return (super.getViewPortHeight() / 2 / 5) * 5;
		}
		
		public MenuView(AbstractMIDlet midlet) {
			super(midlet, "", false);
			init();
		}

		private void init() {
			setPaintScrollBar(false);
			setCommandEnabled(false);
			setPreRender(false);
			setCellRenderer(new MenuItemRenderer());
			setClientBackgroundEnabled(false);
			setScreenDragging(true);

			setFullScreenMode(true);
			setTitle("view.menu.title");
//		    setPaintScrollBar(true);
//		    setScreenDragging(true);
			loadLogoImage();
			
			createMenu();
		}

		protected void paintScreenBackground(Graphics g) {
			super.paintScreenBackground(g);
			Rectangle clientArea = computeClientArea();
	         
			int x = clientArea.location.x;
			int y = clientArea.location.y;
			int width = clientArea.size.width;
	        int height = clientArea.size.height;
  
	        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND).intValue());
	        g.fillRect(x, y, width, height);
 	        if (logoImage != null) {
	        	int imgHeight = logoImage.getHeight();
	           	int imgAllowedHeight = height / 2;
//	           	logger.info("img height: " + imgHeight);
//	           	logger.info("img allowed height: " + imgAllowedHeight);
	        	if (imgHeight < imgAllowedHeight) {
		        	g.drawImage(logoImage, x + width / 2, y + (height / 2) / 2, Graphics.HCENTER | Graphics.VCENTER);
	        	}
	        	else {
		        	logger.info("Image to big");
			        g.setColor(ColorUtil.BLACK);
		        	g.drawString(fallbackLogoImageStr, x + width / 2, y + (height / 2) / 2, Graphics.HCENTER | Graphics.BASELINE);        	
	        	}
	        }
	        else if (fallbackLogoImageStr != null) {
	        	logger.info("Image not found");
		        g.setColor(ColorUtil.BLACK);
	        	g.drawString(fallbackLogoImageStr, x + width / 2, y + (height / 2) / 2, Graphics.HCENTER | Graphics.BASELINE);        	
	        }
	        else {
	        	logger.info("fallbackLogoImageStr error");
	        }
		}

		protected void onShowItemSelected(Object object) {
			MenuItem menuItem = (MenuItem)object;
			if (menuItem.isEnabled()) {
				menuItem.getAction().run(menuItem.getData());
			}
		}

		private void loadLogoImage() {
			try {
				String menuImageStr = getTheme().getString(ThemeConstants.WIDGET_MENU_IMAGE);
				logoImage = ImageUtil.createImageFromClassPath(menuImageStr);
			}
			catch(Throwable t) {
				fallbackLogoImageStr = t.getMessage();
				if (fallbackLogoImageStr == null) {
					fallbackLogoImageStr = t.toString();
				}
				
				logger.warn(t);
			}
		}


		private void createMenu() {
			Menu menu = new Menu();
			menu.addMenuItem(new MenuItem("view.menu.item.station.list", new IAction() {
				
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "dialog.title.warn", "Not implemented");
				}

			}));
			menu.addMenuItem(new MenuItem("view.menu.item.station.list.bookmark", true, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "dialog.title.warn", "Not implemented");
				}
			}));

			menu.addMenuItem(new MenuItem("view.menu.item.pref", true, new IAction() {
				
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "dialog.title.warn", "Not implemented");
				}
			}));

			menu.addMenuItem(new MenuItem("view.menu.item.about", new IAction() {
				
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "dialog.title.warn", "Not implemented");
				}
				
			}));

			menu.addMenuItem(new MenuItem("view.menu.item.exit", new IAction() {
				public void run(Object data) {
					DialogUtil.showYesNoDialog(
						MenuView.this, 
						"dialog.title.question", 
						getMessage("view.menu.item.exit.message"), 
						new YesNoResultCallback() {
							public void onYes(DialogView dialogView, Object data) {
								exit();
							}
							public void onNo(DialogView dialogView, Object data) {
								dialogView.showDisplayable(MenuView.this);
							}
						});
				}
			}));
			
			setMenu(menu);
		}

	}