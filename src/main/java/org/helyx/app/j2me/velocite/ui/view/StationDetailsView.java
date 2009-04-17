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
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.map.google.GoogleMapView;
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
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerFactory;

public class StationDetailsView extends AbstractView {
	
	private static final Logger logger = LoggerFactory.getLogger("STATION_DETAILS_VIEW");
	
	private Image iconImage;
	
	private Station station;
	private StationDetails stationDetails;
	
	private IElementProvider relatedStations;
	
	private boolean allowSearchNearStation = true;

	public StationDetailsView(AbstractMIDlet midlet, Station station)  {
		super(midlet);
		this.station = station;
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("Détail de la station");
		loadIconImage();

		initActions();
		fetchStationDetails();
	}
	
	private void showGoogleMapsView() {
		GoogleMapView googleMapView = new GoogleMapView(getMidlet(), "Plan de la station", new StationPoiInfoAccessor(), station.localization, 15);
		googleMapView.setPreviousDisplayable(StationDetailsView.this);
		googleMapView.setSelectedPoi(station);
		googleMapView.setPoiItems(relatedStations);
		googleMapView.showDisplayable(googleMapView);
		googleMapView.updateMap();
	}
		
	private void initActions() {
		
		setThirdCommand(new Command("Menu", true, new IAction() {

			public void run(Object data) {

				final MenuListView menuListView = new MenuListView(getMidlet(), "Menu", false);

				Menu menu = new Menu();

				menu.addMenuItem(new MenuItem("Voir le plan", new IAction() {
					
					public void run(Object data) {
						showGoogleMapsView();
					}
				}));
				
				if (allowSearchNearStation) {
					menu.addMenuItem(new MenuItem("Voir les stations proches", new IAction() {
						
						public void run(Object data) {
							final MenuListView nearStationMenuListView = new MenuListView(getMidlet(), "Menu", false);

							Menu nearStationMenu = new Menu();
							nearStationMenu.addMenuItem(new MenuItem("Stations à moins de 250 m", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, station, 250, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("Stations à moins de 500 m", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, station, 500, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("Stations à moins de 1 km", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, station, 1000, false, false, false);
								}
			
							}));
							nearStationMenu.addMenuItem(new MenuItem("Stations à moins de 2 km", new IAction() {
								
								public void run(Object data) {
									CartoManager.showStationByDistance(menuListView, nearStationMenuListView, station, 2000, false, false, false);
								}
			
							}));
							
							nearStationMenuListView.setMenu(nearStationMenu);
							nearStationMenuListView.setPreviousDisplayable(menuListView);
							showDisplayable(nearStationMenuListView);
						}

					}));
				}
				
				
				menu.addMenuItem(new MenuItem("Ajouter aux favoris", new IAction() {
					
					public void run(Object data) {
						DialogUtil.showAlertMessage(menuListView, "Attention", "La fonction n'est pas encore implémentée.");
					}

				}));

				menuListView.setMenu(menu);
				menuListView.setPreviousDisplayable(StationDetailsView.this);

				showDisplayable(menuListView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setPrimaryCommand(new Command("Rafraîchir", true, new IAction() {

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
			IProgressTask progressTask = CartoManager.fetchStationDetails(CityManager.findSelectedCity(), station);
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
		catch (CartoManagerException e) {
			e.printStackTrace();
		}
		catch (CityManagerException e) {
			e.printStackTrace();
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
		        g.drawString(" - Station fermée", width + iconImageWidth + 5 + smallFont.stringWidth(stationNumber), height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
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
		        g.drawString(" - Station fermée", width + smallFont.stringWidth(stationNumber), height + mediumFontHeight + 1, Graphics.BASELINE | Graphics.LEFT);
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
        g.fillRect(5, height + 5, canvasWidth - 10, 5 + (mediumFontHeight + 1) * 4 + 5);
        
        height += 5 + 2;
        
        g.setColor(detailsFontMessageColor.intValue());
        g.setFont(mediumFont);
        int veloDispoWidth = mediumFont.stringWidth("Vélos disponibles: ") + 5 + mediumFont.stringWidth(String.valueOf("20"));
        
        int contentLeftPos = canvasWidth / 2 - veloDispoWidth / 2;
        int contentRightPos = canvasWidth / 2 + veloDispoWidth / 2;
        
        String total = stationDetails != null ? String.valueOf(stationDetails.total) : "ND"; 
        String available = stationDetails != null ? String.valueOf(stationDetails.available) : "ND"; 
        String free = stationDetails != null ? String.valueOf(stationDetails.free) : "ND"; 
        String hs = stationDetails != null ? String.valueOf(stationDetails.total - stationDetails.available - stationDetails.free) : "ND"; 
        
        g.drawString("Total vélos: ", contentLeftPos, height + 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString(total, contentRightPos - mediumFont.stringWidth(total), height + 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("Vélos disponibles: ", contentLeftPos, height + (mediumFontHeight + 1) * 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString(available, contentRightPos - mediumFont.stringWidth(available), height + (mediumFontHeight + 1) * 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("Places libres: ", contentLeftPos, height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT); 
        g.drawString(free, contentRightPos - mediumFont.stringWidth(free), height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT);
        g.drawString("Hors service: ", contentLeftPos, height + (mediumFontHeight + 1) * 3, Graphics.TOP | Graphics.LEFT); 
        g.drawString(hs, contentRightPos - mediumFont.stringWidth(hs), height + (mediumFontHeight + 1) * 3, Graphics.TOP | Graphics.LEFT);
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
