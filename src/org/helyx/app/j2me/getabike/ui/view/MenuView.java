package org.helyx.app.j2me.getabike.ui.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.data.city.manager.CityManagerException;
import org.helyx.app.j2me.getabike.ui.view.renderer.StationItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.Theme;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.ColorUtil;
import org.helyx.helyx4me.ui.util.FontUtil;
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
			loadLogoImage();
			
			createMenu();
		}

		protected void paint(Graphics g) {
			Rectangle clientArea = computeClientArea(g);
	         
			int x = clientArea.location.x;
			int y = clientArea.location.y;
			int width = clientArea.size.width;
	        int height = clientArea.size.height;
	       
//	        g.setColor(ColorUtil.WHITE);
//	        g.fillRect(x, y, width, height);     
	        
	        g.setColor(ColorUtil.BLACK);

	        int _height = y + height / 3 - FontUtil.SMALL.getHeight();
	        if (logoImage != null) {
	        	g.drawImage(logoImage, x + width / 2, _height, Graphics.HCENTER | Graphics.VCENTER);
	        	_height += logoImage.getHeight() / 2;
	        }
	        else if (fallbackLogoImageStr != null) {
	        	logger.info(fallbackLogoImageStr);
	        	g.drawString(fallbackLogoImageStr, x + width / 2, y + _height, Graphics.HCENTER | Graphics.BASELINE);        	
	        	_height += FontUtil.LARGE_BOLD.getHeight() / 2;
	        }
	        else {
	        	logger.info("fallbackLogoImageStr error");
	        }

	        int menuItemCount = menu.menuItemCount();
	        
	        _height += ( (height - _height) - (FontUtil.SMALL.getHeight() * menuItemCount) ) / 2;
	        
	        Theme theme = getTheme();
	        for (int i = 0 ; i < menuItemCount ; i++) {
	        	MenuItem menuItem = (MenuItem)menu.getMenuItem(i);
	           
		        Font font = i == selectedIndex ? FontUtil.MEDIUM_BOLD : FontUtil.SMALL;

	    	    Color menuItemColor;
	    	    boolean isMenuEnabled = menuItem.isEnabled();
	    	    if (i == selectedIndex) {
	    	    	menuItemColor = theme.getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT_SELECTED : ThemeConstants.WIDGET_MENU_FONT_SELECTED_DISABLED);
	    	    }
	    	    else {
	    	    	menuItemColor = theme.getColor(isMenuEnabled ? ThemeConstants.WIDGET_MENU_FONT : ThemeConstants.WIDGET_MENU_FONT_DISABLED);
	    	    }
	    	    	
	    	    g.setColor(menuItemColor.intValue());
		        g.setFont(font);
		        g.drawString(getMessage(menuItem.getText()), x + width / 2, y + _height, Graphics.HCENTER | Graphics.BASELINE);
		        
		        _height += FontUtil.SMALL.getHeight();
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
		
		private void showStationListView(City city) {
			if (stationListView == null) {
				stationListView = CartoManager.createStationListView(this, city);
				stationListView.loadListContent();
			}
			else {
				stationListView.setCity(city);
				showDisplayable(stationListView, this);
			}
		}
		
		private void showStations() {
			try {
				String currentCountry = CityManager.getCurrentCountry();
				if (currentCountry == null) {
					selectCountry();
					return ;
				}
				
				City currentCity = CityManager.getCurrentCity();
				if (currentCity == null) {
					selectCity(currentCountry);
					return ;
				}

				// Should not append!
				if (!currentCity.country.equals(currentCountry)) {
					CityManager.clearCurrentCountry();
					CityManager.clearCurrentCity(true);
					DialogUtil.showAlertMessage(this, "dialog.title.error", getMessage("view.menu.show.station.erro.1", new Object[] { currentCity.key,  currentCountry }));
					return ;
				}
				
				showStationListView(currentCity);
			}
			catch (CityManagerException e) {
				DialogUtil.showAlertMessage(this, "dialog.title.error", getMessage("view.menu.show.station.error.2"));
				logger.warn(e);
			}
		}
		
		private void selectCountry() throws CityManagerException {
			CountryListView countryListView = new CountryListView(getMidlet(), true);
			countryListView.setReturnCallback(new IReturnCallback() {

				public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
					String currentCountry = CityManager.getCurrentCountry();
					if (currentCountry != null) {
						showStations();
					}
					else {
						showDisplayable(MenuView.this);
					}
				}
				
			});
			showDisplayable(countryListView);
		}
		
		private void selectCity(String currentCountry) throws CityManagerException {
			CityListView cityListView = new CityListView(getMidlet(), currentCountry, true);
			cityListView.setReturnCallback(new IReturnCallback() {

				public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
					try {
						City currentCity = CityManager.getCurrentCity();
						if (currentCity != null) {
							showStations();
						}
						else {
							showDisplayable(MenuView.this);
						}
					}
					catch(Throwable t) {
						logger.warn(t);
						DialogUtil.showAlertMessage(MenuView.this, "dialog.title.error", getMessage("view.menu.select.city.error.1", t.getMessage()));
					}
				}
				
			});
			showDisplayable(cityListView);
		}

		private void createMenu() {
			menu = new Menu();
			menu.addMenuItem(new MenuItem("view.menu.item.station.list", new IAction() {
				
				public void run(Object data) {
					showStations();
				}

			}));
			menu.addMenuItem(new MenuItem("view.menu.item.station.list.bookmark", false, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "dialog.title.information", getMessage("view.menu.item.station.list.bookmark.not.implemented"));
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