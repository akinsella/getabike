package org.helyx.app.j2me.velocite.ui.view;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.filter.IObjectFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.MathUtil;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;

public class StationDetailsView extends AbstractView {
	
	private static final Log log = LogFactory.getLog("STATION_DETAILS_VIEW");
	
	private Image iconImage;
	
	private Station station;
	private StationDetails stationDetails;

	public StationDetailsView(AbstractMIDlet midlet, Station station)  {
		super(midlet);
		this.station = station;
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("D�tail de la station");
		loadIconImage();

		initActions();
		fetchStationDetails();
	}
		
	private void initActions() {
		setThirdCommand(new Command("Menu", true, new IAction() {

		
			public void run(Object data) {
				Menu menu = new Menu();
				menu.addMenuItem(new MenuItem("Station Proches", new IAction() {
					
					public void run(Object data) {
						StationListView stationListView = new StationListView(getMidlet(), "Station Proches");
						stationListView.setPreviousDisplayable(StationDetailsView.this);
						
						UIStationLoaderProgressListener slpl = new UIStationLoaderProgressListener(stationListView, new IObjectFilter() {
							public boolean matches(Object object) {
								Station station = (Station)object;
								double distanceInMeters = MathUtil.distance(
										StationDetailsView.this.station.localization.lat,
										StationDetailsView.this.station.localization.lng,
										station.localization.lat,
										station.localization.lng, MathUtil.KM) * 1000;
								
								log.info("Distance is: '" + distanceInMeters + "' meters between " + StationDetailsView.this.station + " and " + station);
								return true;
							}
							
						});
						stationListView.loadListContent(slpl);
						
					}

				}));

				MenuListView prefMenuListView = new MenuListView(getMidlet(), "Menu", false);
				prefMenuListView.setMenu(menu);
				prefMenuListView.setPreviousDisplayable(StationDetailsView.this);
				showDisplayable(prefMenuListView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));

	}
	

	private void loadIconImage() {
		try {
			iconImage = ImageUtil.createImageFromClassPath(getTheme().getString(AppThemeConstants.WIDGET_STATION_DETAILS_IMAGE));
		}
		catch (IOException e) {
			log.warn(e);
		}
		
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		log.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.LEFT) {
			returnToPreviousDisplayable();
		}
	}

	private void fetchStationDetails() {
		log.info("Fetching Station Details for Station number: '" + station.number + "'");
		try {
			IProgressTask progressTask = CartoManager.fetchStationDetails(CityManager.findSelectedCity(), station);
			progressTask.addProgressListener(new ProgressAdapter() {

				public void onSuccess(String eventMessage, Object eventData) {
					StationDetailsView.this.log.info("Station Details fetched for Station number: '" + station.number + "'");
					StationDetailsView.this.stationDetails = (StationDetails)eventData;
					StationDetailsView.this.repaint();
				}

			});
			progressTask.start();
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
		        g.drawString(" - Station ferm�e", width + iconImageWidth + 5 + smallFont.stringWidth(stationNumber), height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
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
		        g.drawString(" - Station ferm�e", width + smallFont.stringWidth(stationNumber), height + mediumFontHeight + 1, Graphics.BASELINE | Graphics.LEFT);
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
        int veloDispoWidth = mediumFont.stringWidth("V�los disponibles: ") + 5 + mediumFont.stringWidth(String.valueOf("20"));
        
        int contentLeftPos = canvasWidth / 2 - veloDispoWidth / 2;
        int contentRightPos = canvasWidth / 2 + veloDispoWidth / 2;
        
        String total = stationDetails != null ? String.valueOf(stationDetails.total) : "ND"; 
        String available = stationDetails != null ? String.valueOf(stationDetails.available) : "ND"; 
        String free = stationDetails != null ? String.valueOf(stationDetails.free) : "ND"; 
        String hs = stationDetails != null ? String.valueOf(stationDetails.total - stationDetails.available - stationDetails.free) : "ND"; 
        
        g.drawString("Total v�los: ", contentLeftPos, height + 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString(total, contentRightPos - mediumFont.stringWidth(total), height + 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("V�los disponibles: ", contentLeftPos, height + (mediumFontHeight + 1) * 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString(available, contentRightPos - mediumFont.stringWidth(available), height + (mediumFontHeight + 1) * 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("Places libres: ", contentLeftPos, height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT); 
        g.drawString(free, contentRightPos - mediumFont.stringWidth(free), height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT);
        g.drawString("Hors service: ", contentLeftPos, height + (mediumFontHeight + 1) * 3, Graphics.TOP | Graphics.LEFT); 
        g.drawString(hs, contentRightPos - mediumFont.stringWidth(hs), height + (mediumFontHeight + 1) * 3, Graphics.TOP | Graphics.LEFT);
	}

}
