package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.ui.view.renderer.StationItemRenderer;
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
import org.helyx.helyx4me.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogResultConstants;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
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
			setTitle("Menu");
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
		        g.drawString(menuItem.getText(), x + width / 2, y + _height, Graphics.HCENTER | Graphics.BASELINE);
		        
		        _height += FontUtil.SMALL.getHeight();
	        }
		}

		protected void onKeyPressed(int keyCode) {
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

		protected void onKeyRepeated(int keyCode) {
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
				stationListView = new StationListView(getMidlet(), "Liste des stations");
				stationListView.setPreviousDisplayable(MenuView.this);
				stationListView.setCellRenderer(new StationItemRenderer());
				stationListView.setCity(city);
				stationListView.setAllowMenu(true);
				stationListView.setAllowNested(true);
				stationListView.loadListContent(new UIStationLoaderProgressListener(stationListView));						
			}
			else {
				stationListView.setCity(city);
				stationListView.loadListContent(new UIStationLoaderProgressListener(stationListView));						
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
					DialogUtil.showAlertMessage(this, "Erreur", "La ville s�lectionn�e: '" + currentCity.key + "', ne correspond pas au pays s�lectionn�: '" + currentCountry + "'");
					return ;
				}
				
				showStationListView(currentCity);
			}
			catch (CityManagerException e) {
				showAlertMessage("Erreur", "Probl�me de s�lection de la ville.");
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
						DialogUtil.showAlertMessage(MenuView.this, "Erreur", "Impossible d'obtenir la ville courante: '" + t.getMessage() + "'");
					}
				}
				
			});
			showDisplayable(cityListView);
		}

		private void createMenu() {
			menu = new Menu();
			menu.addMenuItem(new MenuItem("Liste des stations", new IAction() {
				
				public void run(Object data) {
					City currentCity = CityManager.getCurrentCity();
					if (currentCity == null) {
						CityManager.clearCurrentCountry();
						CityManager.clearCurrentCity(true);
					}
					showStations();
				}

			}));
			menu.addMenuItem(new MenuItem("Stations favorites", false, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "Information", "Station favories");
				}
			}));
			menu.addMenuItem(new MenuItem("Itineraire", false, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "Information", "Itin�raire");
				}
			}));
			menu.addMenuItem(new MenuItem("Pr�f�rences", true, new IAction() {
				private PrefListView prefListView;
				
				public void run(Object data) {
					showDisplayable(getPrefListView(), MenuView.this);
				}
				
				private PrefListView getPrefListView() {
					if (prefListView == null) {
						prefListView = new PrefListView(getMidlet());
						prefListView.setPreviousDisplayable(MenuView.this);
					}
					return prefListView;
				}
			}));
			menu.addMenuItem(new MenuItem("A propos", new IAction() {
				private AboutView aboutView;
				
				public void run(Object data) {
					showDisplayable(getAboutView(), MenuView.this);
				}
				
				private AboutView getAboutView() {
					if (aboutView == null) {
						aboutView = new AboutView(getMidlet());
						aboutView.setPreviousDisplayable(MenuView.this);
					}
					return aboutView;
				}
			}));
			menu.addMenuItem(new MenuItem("Quitter", new IAction() {
				public void run(Object data) {
					DialogUtil.showYesNoDialog(MenuView.this, "Question", "Etes-vous s�r de vouloir quitter l'application ?", new AbstractDialogResultCallback() {

						public void onResult(DialogView dialogView, Object data) {
							int resultValue = dialogView.getResultCode();
							switch (resultValue) {
								case DialogResultConstants.YES:
									exit();
									break;
								case DialogResultConstants.NO:
									dialogView.showDisplayable(MenuView.this);
									break;
							}
						}
					});
				}
			}));
		}

	}