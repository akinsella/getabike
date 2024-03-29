package org.helyx.app.j2me.getabike.ui.view.station;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.map.google.POIInfoAccessor;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.BasicReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.task.LoadTaskView;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
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
		setPaintScrollBar(true);
		setScreenDragging(true);
		setTitle("view.station.detail.title");
		loadIconImage();
		initActions();
		stationDetails = station.details;
	}
		
	void showGoogleMapsView() {
		POIInfoAccessor poiInfoAccessor = new StationPoiInfoAccessor();
		UtilManager.showGoogleMapsView(this, "view.station.detail.map.title", poiInfoAccessor, station, relatedStations, 15);
	}
	
	private void initActions() {
		
		setThirdCommand(new Command("command.menu", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				StationDetailsMenuView stationDetailsMenuView = new StationDetailsMenuView(getMidlet(), StationDetailsView.this);
				stationDetailsMenuView.setPreviousDisplayable(StationDetailsView.this);
				
				showDisplayable(stationDetailsMenuView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setPrimaryCommand(new Command("command.refresh", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				fetchStationDetails();
			}
			
		}));

	}
	

	private void loadIconImage() {
		try {
			iconImage = ImageUtil.createImageFromClassPath(getTheme().getString("IMG_STATION"));
		}
		catch (IOException e) {
			logger.warn(e);
		}
		
	}

	public void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		if (logger.isDebugEnabled()) {
			logger.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
		}
	    if (gameAction == GameCanvas.LEFT) {
	    	fireReturnCallback();
		}
	}

	void fetchStationDetails() {
		if (logger.isDebugEnabled()) {
			logger.debug("Fetching Station Details for Station number: '" + station.number + "'");
		}
		
		try {
			IProgressTask progressTask = CartoManager.fetchStationDetails(CityManager.getCurrentCity(), station);
			final LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.station.detail.load.message", progressTask);
			loadTaskView.setReturnCallback(new BasicReturnCallback(this));
			progressTask.addProgressListener(new ProgressAdapter("Loading station details") {

				public void onError(String eventMessage, Object eventData) {
					if (StationDetailsView.logger.isInfoEnabled()) {
						StationDetailsView.logger.info("Error: " + eventMessage + ", data: " + eventData);
					}
					
					Throwable t = (Throwable)eventData;

					DialogUtil.showMessageDialog(
							StationDetailsView.this, 
							"dialog.title.error", 
							getMessage("connection.error") + ": " + ErrorManager.getErrorMessage(getMidlet(), t), 
							new OkResultCallback() {
								public void onOk(DialogView dialogView, Object data) {
									loadTaskView.fireReturnCallback();
								}
							});
				}

				public void onSuccess(String eventMessage, Object eventData) {
					StationDetailsView.logger.info("Station Details fetched for Station number: '" + station.number + "'");
					StationDetailsView.this.stationDetails = (StationDetails)eventData;
					loadTaskView.fireReturnCallback();
				}
				
			});
			showDisplayable(loadTaskView);
			loadTaskView.startTask();
		}
		catch (Throwable t) {
			logger.warn(t);
			DialogUtil.showAlertMessage(
					StationDetailsView.this, 
					"dialog.title.error", 
					getMessage("dialog.error.unexpected") + ": " + ErrorManager.getErrorMessage(getMidlet(), t));
		}
	}

	protected void paint(Graphics g) {
		int canvasWidth = viewCanvas.getWidth();
 
		Font smallFont = FontUtil.SMALL;
		Font smallBoldFont = FontUtil.SMALL_BOLD;
		Font mediumFont = FontUtil.MEDIUM;
		Font mediumBoldFont = FontUtil.MEDIUM_BOLD;
		
		Rectangle clientArea = computeClientArea();
		
		int width = clientArea.location.x ;
		int height = clientArea.location.y;
		
		int smallFontHeight = smallFont.getHeight();
		int mediumFontHeight = mediumFont.getHeight();

        String stationNumber = String.valueOf(station.number);
		
		if (iconImage != null) {
			int iconImageWidth = iconImage.getWidth();
			int iconImageHeight = iconImage.getHeight();

	        int titleZoneHeight = 0;
	        if (iconImageHeight < mediumFontHeight * 2) {
	        	titleZoneHeight = mediumFontHeight * 2;
	        }
	        else {
		        titleZoneHeight = iconImageHeight;
	        }
	        
	        Color detailsBackgroundColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_BACKGROUND);
	        g.setColor(detailsBackgroundColor.intValue());
	        g.fillRect(width, height, canvasWidth, titleZoneHeight);

	        Color detailsFontTitleColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_FONT_TITLE);
	        g.setColor(detailsFontTitleColor.intValue());
	        
	        g.drawImage(iconImage, width + iconImageWidth / 2, height + titleZoneHeight / 2, Graphics.VCENTER | Graphics.HCENTER);
	   
	        g.setFont(mediumFont);
	        g.drawString(station.name, width + iconImageWidth + 5, height + titleZoneHeight / 2 - mediumFontHeight, Graphics.TOP | Graphics.LEFT);
	        
	        g.setFont(smallFont);
	        g.drawString(stationNumber, width + iconImageWidth + 5, height + titleZoneHeight / 2  + 1, Graphics.TOP | Graphics.LEFT);
	        
	        if (!station.open) {
		        Color detailsErrorColor = getTheme().getColor(ThemeConstants.WIDGET_DETAILS_ERROR);
	        	g.setColor(detailsErrorColor.intValue());
	        	g.setFont(smallBoldFont);
		        g.drawString(" - " + getMessage("view.station.detail.station.closed"), width + iconImageWidth + 5 + smallFont.stringWidth(stationNumber), height + titleZoneHeight / 2 + mediumFontHeight + smallFontHeight / 2, Graphics.TOP | Graphics.LEFT);
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
		        g.drawString(" - " + getMessage("view.station.detail.station.closed"), width + smallFont.stringWidth(stationNumber), height + mediumFontHeight + 1, Graphics.TOP | Graphics.LEFT);
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
        int maxKeyWidth = mediumFont.stringWidth("V�los disponibles: ") + 5 + mediumFont.stringWidth(String.valueOf("WWW"));
        
        String total = stationDetails != null && stationDetails.total >= 0 ? String.valueOf(stationDetails.total) : "ND"; 
        String available = stationDetails != null && stationDetails.available >= 0 ? String.valueOf(stationDetails.available) : "ND"; 
        String free = stationDetails != null && stationDetails.free >= 0 ? String.valueOf(stationDetails.free) : "ND"; 
        String hs = stationDetails != null && stationDetails.hs >= 0 ? String.valueOf(stationDetails.total - stationDetails.available - stationDetails.free) : "ND"; 
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

	public City getCity() {
		return city;
	}

	public Station getStation() {
		return station;
	}

}
