package org.helyx.app.j2me.getabike.ui.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.graphics.Shade;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.ColorUtil;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.util.GraphicsUtil;
import org.helyx.helyx4me.ui.util.ImageUtil;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class MenuView extends AbstractView {
	
		private static final Logger logger = Logger.getLogger("MENU_VIEW");
		
		private Image logoImage;
		private String fallbackLogoImageStr;
		private Menu menu;
		
		private int selectedIndex = 0;

		private StationListView stationListView;

		
		public MenuView(AbstractMIDlet midlet) {
			super(midlet);
			init();
		}

		private void init() {
			setFullScreenMode(true);
			setTitle("view.menu.title");
//		    setPaintScrollBar(true);
//		    setScreenDragging(true);
			loadLogoImage();
			
			createMenu();
		}

		protected void paint(Graphics g) {
			Rectangle clientArea = computeClientArea();
	         
			int x = clientArea.location.x;
			int y = clientArea.location.y;
			int width = clientArea.size.width;
	        int height = clientArea.size.height;
  
	        
 	        if (logoImage != null) {
	        	int imgHeight = logoImage.getHeight();
	           	int imgAllowedHeight = height / 2;
	           	logger.info("img height: " + imgHeight);
	           	logger.info("img allowed height: " + imgAllowedHeight);
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

	        int menuItemCount = menu.menuItemCount();
	        int _height = y + height / 2;
	        
    	    Rectangle mRect = new Rectangle(x, _height, width, height / 2);
	        
	        for (int i = 0 ; i < menuItemCount ; i++) {
	        	MenuItem menuItem = (MenuItem)menu.getMenuItem(i);

		        _height = y + height / 2 + i * (mRect.size.height / menuItemCount);
	        	
		        Font font = i == selectedIndex ? FontUtil.MEDIUM_BOLD : FontUtil.SMALL;

	    	    Color menuItemColor;
	    	    boolean isMenuEnabled = menuItem.isEnabled();
	    	    Rectangle bRect = new Rectangle(
	    	    		mRect.location.x, 
	    	    		_height,
	    	    		mRect.size.width,
	    	    		(int)(mRect.size.height / menuItemCount));

	    	    if (i == selectedIndex) {
	    	    	menuItemColor = getTheme().getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT_SELECTED : ThemeConstants.WIDGET_MENU_FONT_SELECTED_DISABLED);
	    	    }
	    	    else {
	    	    	menuItemColor = getTheme().getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT : ThemeConstants.WIDGET_MENU_FONT_DISABLED);
	    	    }
	    	    

    	    	g.setColor(ColorUtil.DARK_GREY);
    	    	Rectangle  bRect2 = new Rectangle(bRect.location.x + 5 + 1, bRect.location.y + 2 + 1, bRect.size.width - 10 - 1, bRect.size.height - 4 - 1);
    	    	Shade shade = i != selectedIndex ?
    	    		new Shade(getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_DARK), getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_LIGHT)) :
    	    		new Shade(getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_DARK_SELECTED), getTheme().getColor(ThemeConstants.WIDGET_MENU_BG_SHADE_LIGHT_SELECTED));
    	    	GraphicsUtil.fillShade(g, bRect2, shade, true);
    	    	g.setColor(ColorUtil.DARK_GREY);
    	    	g.drawLine(bRect.location.x + 5 + 1, bRect.location.y + 2, bRect.location.x + 5 + bRect.size.width - 10 - 1, bRect.location.y + 2);
     	    	g.drawLine(bRect.location.x + 5 + 1, bRect.location.y + 2 + bRect.size.height - 4, bRect.location.x + 5 + bRect.size.width - 10 - 1, bRect.location.y + 2 + bRect.size.height - 4);
       	    	g.drawLine(bRect.location.x + 5, bRect.location.y + 2 + 1, bRect.location.x + 5, bRect.location.y + 2 + bRect.size.height - 4 - 1);
       	    	g.drawLine(bRect.location.x + 5 + bRect.size.width - 10, bRect.location.y + 2 + 1, bRect.location.x + 5 + bRect.size.width - 10, bRect.location.y + 2 + bRect.size.height - 4 - 1);
       	    	
	    	    g.setColor(menuItemColor.intValue());
		        g.setFont(font);
		        g.drawString(
		        		getMessage(menuItem.getText()), 
		        		bRect.location.x + bRect.size.width / 2, 
		        		bRect.location.y + bRect.size.height / 2 + (font.getHeight() - font.getBaselinePosition()), 
		        		Graphics.HCENTER | Graphics.BASELINE);
		        
	        }
		}
		
		public void onKeyPressed(int keyCode) {
			int gameAction = viewCanvas.getGameAction(keyCode);
		    if (gameAction == GameCanvas.DOWN) {
		    	scrollDown();
		    }
		    else if (gameAction == GameCanvas.UP) {
				scrollUp();
			}
		    else if (gameAction == GameCanvas.RIGHT) {
		    	executeSelectedMenuAction();
			}
		    else if (gameAction == GameCanvas.FIRE) {
				executeSelectedMenuAction();
			}
		}

		private void executeSelectedMenuAction() {
			MenuItem menuItem = (MenuItem)menu.getMenuItem(selectedIndex);
			if (menuItem.isEnabled()) {
				menuItem.getAction().run(menuItem.getData());
			}
		}

		public void onKeyRepeated(int keyCode) {
			int gameAction = viewCanvas.getGameAction(keyCode);
		    if (gameAction == GameCanvas.DOWN) {
		    	scrollDown();
		    }
		    else if (gameAction == GameCanvas.UP) {
				scrollUp();
			}
		}

		private void scrollUp() {
			if (selectedIndex > 0) {
				selectedIndex--;
			}
//			else {
//				selectedIndex = menu.menuItemCount() - 1;
//			}

			viewCanvas.repaint();
		}

		private void scrollDown() {
			if (selectedIndex + 1 < menu.menuItemCount()) {
				selectedIndex++;
			}
//			else {
//				selectedIndex = 0;
//			}
			
			viewCanvas.repaint();
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
		
		private void showStationListView(City city, boolean showOnlyBookmarks) {
			if (stationListView == null || !stationListView.getCity().key.equals(city.key)) {
				stationListView = CartoManager.createStationListView(this, city);
				stationListView.setShowBookmarks(showOnlyBookmarks);
				stationListView.loadListContent();
			}
			else {
				stationListView.setCity(city);
				stationListView.setShowBookmarks(showOnlyBookmarks);
				stationListView.filterAndSort();
				showDisplayable(stationListView, this);
			}
		}
		
		private void showStations() {
			showStations(false);
		}
		
		private void showStations(final boolean showOnlyBookmarks) {
			City currentCity = CityManager.getCurrentCity();
			if (currentCity == null) {
				CityManager.selectCity(this, new IReturnCallback() {
					public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
						try {
							City currentCity = (City)data;
							if (currentCity != null) {
								showStationListView(currentCity, showOnlyBookmarks);
							}
							else {
								showDisplayable(MenuView.this);
							}
						}
						catch(Throwable t) {
							logger.warn(t);
							DialogUtil.showAlertMessage(MenuView.this, "dialog.title.error", getMessage("view.menu.show.station.error.1"));
						}
					}
				});
			}
			else {
				showStationListView(currentCity, showOnlyBookmarks);
			}
		}

		private void createMenu() {
			menu = new Menu();
			menu.addMenuItem(new MenuItem("view.menu.item.station.list", new IAction() {
				
				public void run(Object data) {
					showStations();
				}

			}));
			menu.addMenuItem(new MenuItem("view.menu.item.station.list.bookmark", true, new IAction() {
				public void run(Object data) {
					showStations(true);
				}
			}));
//			menu.addMenuItem(new MenuItem("view.menu.item.itinerary", false, new IAction() {
//				public void run(Object data) {
//					DialogUtil.showAlertMessage(MenuView.this, getMessage("view.menu.item.itinerary"), getMessage("view.menu.item.itinerary.not.implemented"));
//				}
//			}));
			menu.addMenuItem(new MenuItem("view.menu.item.pref", true, new IAction() {
				
				public void run(Object data) {
					showDisplayable(getPrefListView(), MenuView.this);
				}
				
				private PrefListView getPrefListView() {
					PrefListView prefListView = new PrefListView(getMidlet());
					prefListView.setPreviousDisplayable(MenuView.this);
					
					return prefListView;
				}
			}));
			menu.addMenuItem(new MenuItem("view.menu.item.about", new IAction() {
				
				public void run(Object data) {
					showDisplayable(getAboutView(), MenuView.this);
				}
				
				private AboutView getAboutView() {
					AboutView aboutView = new AboutView(getMidlet());
					aboutView.setPreviousDisplayable(MenuView.this);
					
					return aboutView;
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
		}

	}