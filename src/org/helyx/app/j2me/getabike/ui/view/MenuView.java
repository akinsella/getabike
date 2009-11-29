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
package org.helyx.app.j2me.getabike.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.ui.view.contact.ContactListView;
import org.helyx.app.j2me.getabike.ui.view.renderer.DistanceStationItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.renderer.MenuItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.renderer.StationItemRenderer;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.app.j2me.getabike.util.ApiUtil;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.app.j2me.getabike.util.LocationManager;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.BasicReturnCallback;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.ColorUtil;
import org.helyx.helyx4me.ui.util.ImageUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.helyx4me.ui.view.support.menu.MenuListView;
import org.helyx.helyx4me.ui.view.support.task.LoadTaskView;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class MenuView extends MenuListView {
	
		private static final Logger logger = Logger.getLogger("MENU_VIEW");
		
		private Image logoImage;
		private String fallbackLogoImageStr;

		private StationListView stationListView;

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
		        	g.drawString(fallbackLogoImageStr, x + width / 2, y + (height / 2) / 2, Graphics.HCENTER | Graphics.TOP);        	
	        	}
	        }
	        else if (fallbackLogoImageStr != null) {
	        	logger.info("Image not found");
		        g.setColor(ColorUtil.BLACK);
	        	g.drawString(fallbackLogoImageStr, x + width / 2, y + (height / 2) / 2, Graphics.HCENTER | Graphics.TOP);        	
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
					showStations(0);
				}

			}));
			menu.addMenuItem(new MenuItem("view.menu.item.station.list.bookmark", true, new IAction() {
				public void run(Object data) {
					showStations(1);
				}
			}));

			if (ApiUtil.supportLocationApi()) {
				menu.addMenuItem(new MenuItem("view.menu.item.station.list.nearby", true, new IAction() {
					public void run(Object data) {
			    		boolean opeModeSetted = PrefManager.containsPref(PrefConstants.COST_ALLOWED_GEO_LOCALIZATION);

			    		if (!opeModeSetted) {
							UtilManager.changeGeoLocMode(MenuView.this, new IReturnCallback() {
								public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
									locateAndShowNearbyStations();
								}
							});
			    		}
			    		else {
			    			locateAndShowNearbyStations();
			    		}
					}

					private void locateAndShowNearbyStations() {
					    try {
					    	
				    		final boolean opeModeEnabled = PrefManager.containsPref(PrefConstants.COST_ALLOWED_GEO_LOCALIZATION);

				    		IProgressTask progressTask = new AbstractProgressTask("LOCATION_API") {
								
								public Runnable getRunnable() {
									return new Runnable() {
										
										public void run() {
											try {
												getProgressDispatcher().fireEvent(EventType.ON_START);

												Point location = LocationManager.getLocations(opeModeEnabled);
												
									    		if (location != null) {
									    		    getProgressDispatcher().fireEvent(EventType.ON_SUCCESS, "LCOATION OK", location);
									    		}
									    		else {
									    		    getProgressDispatcher().fireEvent(EventType.ON_ERROR, "LCOATION KO", new RuntimeException("Location enable to locate device actually"));
									    		}
											}
											catch(Throwable t) {
								    		    getProgressDispatcher().fireEvent(EventType.ON_ERROR, "LCOATION KO", t);
											}
										}
									};
								}
							};
							
							final LoadTaskView loadTaskView = new LoadTaskView(MenuView.this.getMidlet(), "view.station.detail.load.message", progressTask);
							loadTaskView.setReturnCallback(new BasicReturnCallback(MenuView.this));
							progressTask.addProgressListener(new ProgressAdapter("LOCATION_ATTEMPT") {

								public void onError(String eventMessage, Object eventData) {
									if (MenuView.logger.isInfoEnabled()) {
										MenuView.logger.info("Error: " + eventMessage + ", data: " + eventData);
									}
									
									Throwable t = (Throwable)eventData;
									MenuView.logger.warn(t);
									
									DialogUtil.showMessageDialog(
											MenuView.this, 
											"dialog.title.error", 
											MenuView.this.getMessage("dialog.title.error") + ": " + ErrorManager.getErrorMessage(MenuView.this.getMidlet(), t), 
											new OkResultCallback() {
												public void onOk(DialogView dialogView, Object data) {
													loadTaskView.fireReturnCallback();
												}
											});
									
								}

								public void onSuccess(String eventMessage, Object eventData) {
									location = (Point)eventData;	
									showStations(2);
								}
								
							});
							
							loadTaskView.startTask();
					    }
				    	catch(Throwable t) {
				    		location = null;
				    		logger.warn(t);
							DialogUtil.showAlertMessage(MenuView.this, 
									"dialog.title.warn", 
									getMessage("view.menu.item.location.not.available"));
				    	}
					}
				}));
			}

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

			if (ApiUtil.supportPimApi()) {
				menu.addMenuItem(new MenuItem("view.menu.item.send.to.contact", true, new IAction() {
					
					public void run(Object data) {
						getContactListView().loadListContent();
					}
					
					private ContactListView getContactListView() {
						ContactListView contactListView = new ContactListView(getMidlet(), "Liste des contacts");
						contactListView.setPreviousDisplayable(MenuView.this);
						return contactListView;
					}
				}));
			}

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
			
			setMenu(menu);
		}


		Point location;
		
		private void showStationListView(City city, int type) {
			if (stationListView == null || !stationListView.getCity().key.equals(city.key)) {
				stationListView = CartoManager.createStationListView(this, city);
				stationListView.setShowBookmarks(type == 1);
				stationListView.setLocation(type == 2 ? location : null);
				stationListView.setReferentStation(type == 2 ? null : null);
				stationListView.setCellRenderer(type == 2 ? new DistanceStationItemRenderer(true) : new StationItemRenderer());
				stationListView.loadListContent();
			}
			else {
				stationListView.setCity(city);
				stationListView.setShowBookmarks(type == 1);
				stationListView.setLocation(type == 2 ? location : null);
				stationListView.setReferentStation(type == 2 ? null : null);
				stationListView.setCellRenderer(type == 2 ? new DistanceStationItemRenderer(true) : new StationItemRenderer());
				stationListView.filterAndSort();
				showDisplayable(stationListView, this);
			}
		}
		
		private void showStations(final int type) {
			City currentCity = CityManager.getCurrentCity();
			if (currentCity == null) {
				CityManager.selectCity(this, new IReturnCallback() {
					public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
						try {
							City currentCity = (City)data;
							if (currentCity != null) {
								showStationListView(currentCity, type);
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
				showStationListView(currentCity, type);
			}
		}

	}