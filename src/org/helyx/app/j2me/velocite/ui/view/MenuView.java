package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
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

		private StationListView stationListView;
		
		private void showStationListView() {
			
			if (stationListView == null) {
				stationListView = new StationListView(getMidlet(), "Liste des stations");
				stationListView.setPreviousDisplayable(MenuView.this);
				stationListView.setCellRenderer(new StationItemRenderer());
				stationListView.setAllowMenu(true);
				stationListView.setAllowNested(true);
				stationListView.loadListContent(new UIStationLoaderProgressListener(stationListView));						
			}
			else {
				stationListView.loadListContent(new UIStationLoaderProgressListener(stationListView));						
			}

		}

		private void createMenu() {
			menu = new Menu();
			menu.addMenuItem(new MenuItem("Liste des stations", new IAction() {
				
				public void run(Object data) {
					City selectedCity;
					try {
						selectedCity = CityManager.findSelectedCity();
						if (selectedCity == null) {
							CityListView cityListView = new CityListView(getMidlet(), true);
							cityListView.setReturnCallback(new IReturnCallback() {

								public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
									
									try {
										City city = CityManager.findSelectedCity();
										if (city != null) {
											showStationListView();
										}
										else {
											showAlertMessage("Erreur", "Aucune ville sélectionnée. Vous devez sélectionner une ville.");
										}
									}
									catch (CityManagerException e) {
										logger.warn(e);
										showAlertMessage("Erreur", "Aucune ville sélectionnée. Vous devez sélectionner une ville.");
									}
								}
								
							});
							showDisplayable(cityListView);
						}
						else {
							showStationListView();
						}
					}
					catch (CityManagerException e) {
						showAlertMessage("Erreur", "Problème de sélection de la ville.");
						logger.warn(e);
					}
				}

			}));
			menu.addMenuItem(new MenuItem("Stations favorites", false, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "Information", "Station favories");
				}
			}));
			menu.addMenuItem(new MenuItem("Itineraire", false, new IAction() {
				public void run(Object data) {
					DialogUtil.showAlertMessage(MenuView.this, "Information", "Itinéraire");
				}
			}));
			menu.addMenuItem(new MenuItem("Préférences", true, new IAction() {
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
					DialogUtil.showYesNoDialog(MenuView.this, "Question", "Etes-vous sûr de vouloir quitter l'application ?", new AbstractDialogResultCallback() {

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