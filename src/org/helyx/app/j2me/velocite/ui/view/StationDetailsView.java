package org.helyx.app.j2me.velocite.ui.view;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.velocite.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.map.google.POIInfoAccessor;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.util.ImageUtil;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.list.IElementProvider;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.helyx4me.ui.widget.ImageSet;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class StationDetailsView extends AbstractView {
	
	private static final Logger logger = Logger.getLogger("STATION_DETAILS_VIEW");
	
	private Image iconImage;
	
	private City city;
	private Station station;
	private StationDetails stationDetails;
	
	private IElementProvider relatedStations;
	
	private boolean allowSearchNearStation = true;

	public StationDetailsView(AbstractMIDlet midlet, City city, Station station)  {
		super(midlet);
		this.city = city;
		this.station = station;
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("view.station.detail.title");
		loadIconImage();
		initActions();
		if (station.details == null) {
			fetchStationDetails();
		}
		else {
			stationDetails = station.details;
		}
	}
		
	private void showGoogleMapsView() {
		String title = getMessage("view.station.detail.map.title");
		POIInfoAccessor poiInfoAccessor = new StationPoiInfoAccessor();
		UtilManager.showGoogleMapsView(this, title, poiInfoAccessor, station, relatedStations, 15);
	}
	
	private void initActions() {
		
		setThirdCommand(new Command("command.menu", true, new IAction() {

			public void run(Object data) {
	
				final MenuListView menuListView = new MenuListView(getMidlet(), getMessage("view.station.detail.menu.title"), false);

				Menu menu = new Menu();

				if (city.localization) {
					menu.addMenuItem(new MenuItem("view.station.detail.menu.item.map", new ImageSet(getTheme().getString("IMG_MAP")), new IAction() {
						
						public void run(Object data) {
							showGoogleMapsView();
						}
					}));
				}
				
				if (allowSearchNearStation && city.localization) {
					menu.addMenuItem(new MenuItem("view.station.detail.menu.item.near.station", new ImageSet(getTheme().getString("IMG_NEAR")), new IAction() {
						
						public void run(Object data) {
							final MenuListView nearStationMenuListView = new MenuListView(getMidlet(), getMessage("view.station.detail.item.near.station.menu.title"), false);

							Menu nearStationMenu = new Menu();
							nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.250", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, city, station, 250, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.500", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, city, station, 500, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.1000", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, city, station, 1000, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.2000", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, city, station, 2000, false, false, false);
								}
			
							}));
							
							nearStationMenuListView.setMenu(nearStationMenu);
							nearStationMenuListView.setPreviousDisplayable(menuListView);
							showDisplayable(nearStationMenuListView);
						}

					}));
				}
				
				
				menu.addMenuItem(new MenuItem("view.station.detail.menu.item.bookmark.add", new ImageSet(getTheme().getString("IMG_STAR")), new IAction() {
					
					public void run(Object data) {
						DialogUtil.showAlertMessage(menuListView, getMessage("dialog.title.warn"),  getMessage("view.station.detail.menu.item.bookmark.add.not.implemented"));
					}

				}));

				menuListView.setMenu(menu);
				menuListView.setPreviousDisplayable(StationDetailsView.this);

				showDisplayable(menuListView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("command.back", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setPrimaryCommand(new Command("command.refresh", true, new IAction() {

			public void run(Object data) {
				fetchStationDetails();
			}
			
		}));

	}
	

	private void loadIconImage() {
		try {
			iconImage = ImageUtil.createImageFromClassPath(getTheme().getString(AppThemeConstants.WIDGET_STATION_DETAILS_IMAGE));
		}
		catch (IOException e) {
			logger.warn(e);
		}
		
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		logger.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.LEFT) {
	    	fireReturnCallback();
		}
	}

	private void fetchStationDetails() {
		logger.info("Fetching Station Details for Station number: '" + station.number + "'");
		try {
			IProgressTask progressTask = CartoManager.fetchStationDetails(CityManager.getCurrentCity(), station);
			progressTask.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {

				public void onSuccess(String eventMessage, Object eventData) {
					StationDetailsView.this.logger.info("Station Details fetched for Station number: '" + station.number + "'");
					StationDetailsView.this.stationDetails = (StationDetails)eventData;
					StationDetailsView.this.repaint();
				}

			});
			progressTask.execute();
			
			progressTask.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {

				public void onStart() {
					setPrimaryCommandEnabled(false);
				}
				
				public void onBeforeCompletion(int eventType, String eventMessage, Object eventData) {
					setPrimaryCommandEnabled(true);
				}
				
			});
//			TaskManager.runLoadTaskView("Recherche des infos de station", progressTask, getMidlet(), StationDetailsView.this, StationDetailsView.this);
		}
		catch (Throwable t) {
			logger.warn(t);
		}
	}

	protected void paint(Graphics g) {
		int canvasWidth = viewCanvas.getWidth();
 
		Font smallFont = FontUtil.SMALL;
		Font smallBoldFont = FontUtil.SMALL_BOLD;
		Font mediumFont = FontUtil.MEDIUM;
		Font mediumBoldFont = FontUtil.MEDIUM_BOLD;
		
		Rectangle clientArea = computeClientArea(g);
		int width = clientArea.location.x + 1;
		int height = clientArea.location.y + 1;
		int smallFontHeight = smallFont.getHeight();
		int mediumFontHeight = mediumFont.getHeight();
		int mediumBoldFontHeight = mediumBoldFont.getHeight();

        String stationNumber = String.valueOf(station.number);
		
		if (iconImage != null) {
			int iconImageWidth = iconImage.getWidth();
			int iconImageHeight = iconImage.getHeight();

	        int titleZoneHeight = 0;
	        if (iconImageHeight < mediumBoldFontHeight + smallFontHeight) {
	        	titleZoneHeight = mediumBoldFontHeight + smallFontHeight;
	        }
	        else {
		        titleZoneHeight = iconImageHeight;
	        }
	        
	        Color detailsBackgroundColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_BACKGROUND);
	        g.setColor(detailsBackgroundColor.intValue());
	        g.fillRect(0, 0, canvasWidth, height + titleZoneHeight + 1);

	        Color detailsFontTitleColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_FONT_TITLE);
	        g.setColor(detailsFontTitleColor.intValue());
	        
	        g.drawImage(iconImage, width + iconImageWidth / 2, height + titleZoneHeight / 2, Graphics.VCENTER | Graphics.HCENTER);
	        g.setFont(mediumFont);
	        g.drawString(station.name, width + iconImageWidth + 5, height + titleZoneHeight / 2 + mediumFontHeight + 2 - mediumBoldFont.getBaselinePosition() - mediumFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        
	        g.setFont(smallFont);
	        g.drawString(stationNumber, width + iconImageWidth + 5, height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        
	        if (!station.open) {
		        Color detailsErrorColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_ERROR);
	        	g.setColor(detailsErrorColor.intValue());
	        	g.setFont(smallBoldFont);
		        g.drawString(" - " + getMessage("view.station.detail.station.closed"), width + iconImageWidth + 5 + smallFont.stringWidth(stationNumber), height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        }
	        height += titleZoneHeight + 2;
		}
		else {
	        Color detailsBackgroundColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_BACKGROUND);
	        g.setColor(detailsBackgroundColor.intValue());
	        g.fillRect(0, 0, canvasWidth, height + mediumFontHeight + 1 + smallFontHeight + 1);

	        
	        Color detailsFontMessageColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_FONT_MESSAGE);
			g.setColor(detailsFontMessageColor.intValue());
	        g.setFont(mediumFont);
	        g.drawString(station.name, width, height, Graphics.TOP | Graphics.HCENTER); 
        	g.setFont(smallBoldFont);
	        g.drawString(String.valueOf(station.number), width, height + mediumFontHeight + 1, Graphics.TOP | Graphics.HCENTER); 
	        
	        if (!station.open) {
		        Color detailsErrorColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_ERROR);
	        	g.setColor(detailsErrorColor.intValue());
		        g.drawString(" - " + getMessage("view.station.detail.station.closed"), width + smallFont.stringWidth(stationNumber), height + mediumFontHeight + 1, Graphics.BASELINE | Graphics.LEFT);
	        }

	        height += mediumFontHeight + 1 + smallFontHeight + 1;
	    }
		
        height += 5;
        
        Font addressFont;
        if (mediumFont.stringWidth(station.address) < canvasWidth - 12) {
        	addressFont = mediumFont;
        }
        else if (smallBoldFont.stringWidth(station.address) < canvasWidth - 12) {
        	addressFont = smallBoldFont;
        }
        else {
        	addressFont = smallFont;
        }
        

        Color detailsBackgroundColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_BACKGROUND);
        g.setColor(detailsBackgroundColor.intValue());
        g.fillRect(5, height, canvasWidth - 10, 5 + addressFont.getHeight() + 1 + smallFontHeight + 5);

        Color detailsFontMessageColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_FONT_MESSAGE);
        g.setColor(detailsFontMessageColor.intValue());

        height += 5;
        g.setFont(addressFont);
        
        g.drawString(
        		station.address, 
        		canvasWidth / 2, height, 
        		Graphics.TOP | Graphics.HCENTER);
        
        height += 1 + addressFont.getHeight();
        
        g.setFont(smallFont);
        g.drawString(
        		station.zipCode + " - " + station.city, 
        		canvasWidth / 2, height, 
        		Graphics.TOP | Graphics.HCENTER);
        
        height += smallFontHeight + 5;

        g.setColor(detailsBackgroundColor.intValue());
        g.fillRect(5, height + 5, canvasWidth - 10, 5 + (mediumFontHeight + 1) * (4 + (city.tpe ? 1 : 0) + (city.bonus ? 1 : 0)) + 5);
        
        height += 5 + 2;
        
        g.setColor(detailsFontMessageColor.intValue());
        g.setFont(mediumFont);
        int maxKeyWidth = mediumFont.stringWidth("Vélos disponibles: ") + 5 + mediumFont.stringWidth(String.valueOf("WWW"));
        
        String total = stationDetails != null ? String.valueOf(stationDetails.total) : "ND"; 
        String available = stationDetails != null ? String.valueOf(stationDetails.available) : "ND"; 
        String free = stationDetails != null ? String.valueOf(stationDetails.free) : "ND"; 
        String hs = stationDetails != null ? String.valueOf(stationDetails.total - stationDetails.available - stationDetails.free) : "ND"; 
        String tpe = stationDetails != null ? ((station.tpe || stationDetails.tpe) ? "Oui" : "Non") : "ND"; 
        String bonus = stationDetails != null ? (station.bonus ? "Oui" : "Non") : "ND"; 
         
        int line = 0;
        showDetailLine(g, line, getMessage("view.station.detail.station.bike.total") + ": ", total, height, maxKeyWidth);
        line++;
        showDetailLine(g, line, getMessage("view.station.detail.station.bike.available") + ": ", available, height, maxKeyWidth);
        line++;
        showDetailLine(g, line, getMessage("view.station.detail.station.attach.free") + ": ", free, height, maxKeyWidth);
        line++;
        showDetailLine(g, line, getMessage("view.station.detail.station.bike.ko") + ": ", hs, height, maxKeyWidth);
        line++;
        if (city.tpe) {
        showDetailLine(g, line, getMessage("view.station.detail.station.tpe") + ": ", tpe, height, maxKeyWidth);
        line++;        	
        }
        if (city.bonus) {
        	showDetailLine(g, line, getMessage("view.station.detail.station.bonus") + ":  ", bonus, height, maxKeyWidth);
        }
 	}
	
	private void showDetailLine(Graphics g, int line, String key, String value, int height, int maxKeyWidth) {
		 
		Font mediumFont = FontUtil.MEDIUM;
		
		int mediumFontHeight = mediumFont.getHeight();
	
		int canvasWidth = viewCanvas.getWidth();
        int contentLeftPos = canvasWidth / 2 - maxKeyWidth / 2;
        int contentRightPos = canvasWidth / 2 + maxKeyWidth / 2;

        g.drawString(key, contentLeftPos, height + (mediumFontHeight + 1) * line, Graphics.TOP | Graphics.LEFT); 
        g.drawString(value, contentRightPos - mediumFont.stringWidth(value), height + (mediumFontHeight + 1) * line, Graphics.TOP | Graphics.LEFT);

	}

	public boolean isAllowSearchNearStation() {
		return allowSearchNearStation;
	}

	public IElementProvider getRelatedStations() {
		return relatedStations;
	}

	public void setRelatedStations(IElementProvider relatedStations) {
		this.relatedStations = relatedStations;
	}

	public void setAllowSearchNearStation(boolean allowSearchNearStation) {
		this.allowSearchNearStation = allowSearchNearStation;
	}

}
