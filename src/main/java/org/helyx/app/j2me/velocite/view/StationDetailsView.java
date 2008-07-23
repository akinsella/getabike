package org.helyx.app.j2me.velocite.view;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.rms.DefaultRecordEnumeration;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.widget.ColorUtil;
import org.helyx.app.j2me.lib.ui.widget.FontUtil;
import org.helyx.app.j2me.lib.ui.widget.IAction;
import org.helyx.app.j2me.lib.ui.widget.action.item.ActionItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.carto.service.IStationPersistenceService;
import org.helyx.app.j2me.velocite.data.carto.service.StationPersistenceService;

public class StationDetailsView extends AbstractCanvas {
	
	private static final String CAT = "STATION_DETAILS_VIEW";
	
	private Image iconImage;
	
	private Station station;

	public StationDetailsView(MIDlet midlet, Station station)  {
		super(midlet);
		this.station = station;
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("Détail de la station");
		loadIconImage();

		initActions();
//		StationDetails stationDetails = getStationDetails(station.number);
	}
		
	private void initActions() {
		
		setSecondaryAction(new ActionItem("Retour", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
	}

	private void loadIconImage() {
		try {
			iconImage = ImageUtil.createImageFromClassPath("/vcIcon.png");
		}
		catch (IOException e) {
			Log.warn(CAT, e);
		}
		
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = canvas.getGameAction(keyCode);
		Log.info(CAT, "[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
	    if (gameAction == GameCanvas.LEFT) {
			returnToPreviousDisplayable();
		}
	}

	private StationDetails getStationDetails(int stationNumber) {
		IStationPersistenceService stationPersistenceService = null;
		DefaultRecordEnumeration stationEnumeration = null;

		try {
			stationPersistenceService = new StationPersistenceService();
			StationDetails stationDetails = stationPersistenceService.findStationDetailsByNumber(stationNumber);
			return stationDetails;
		}
    	finally {
    		if (stationEnumeration != null) {
    			stationEnumeration.destroy();
    		}
    		if (stationPersistenceService != null) {
    			stationPersistenceService.dispose();
    		}
    	}  	

	}

	protected void paint(Graphics g) {
		int canvasWidth = canvas.getWidth();
 
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
	        
	        g.setColor(ColorUtil.WIDGET_DETAILS_BACKGROUND);
	        g.fillRect(0, 0, canvasWidth, height + titleZoneHeight + 1);

	        g.setColor(ColorUtil.WIDGET_DETAILS_FONT_TITLE);
	        
	        g.drawImage(iconImage, width + iconImageWidth / 2, height + titleZoneHeight / 2, Graphics.VCENTER | Graphics.HCENTER);
	        g.setFont(mediumFont);
	        g.drawString(station.name, width + iconImageWidth + 5, height + titleZoneHeight / 2 + mediumFontHeight + 2 - mediumBoldFont.getBaselinePosition() - mediumFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        
	        g.setFont(smallFont);
	        g.drawString(stationNumber, width + iconImageWidth + 5, height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        
	        if (!station.open) {
	        	g.setColor(ColorUtil.WIDGET_DETAILS_ERROR);
	        	g.setFont(smallBoldFont);
		        g.drawString(" - Station fermée", width + iconImageWidth + 5 + smallFont.stringWidth(stationNumber), height + titleZoneHeight / 2 + mediumFontHeight - mediumBoldFont.getBaselinePosition() + smallFontHeight / 2, Graphics.BASELINE | Graphics.LEFT);
	        }
	        height += titleZoneHeight + 2;
		}
		else {
	        g.setColor(ColorUtil.WIDGET_DETAILS_BACKGROUND);
	        g.fillRect(0, 0, canvasWidth, height + mediumFontHeight + 1 + smallFontHeight + 1);

	        
			g.setColor(ColorUtil.WIDGET_DETAILS_FONT_MESSAGE);
	        g.setFont(mediumFont);
	        g.drawString(station.name, width, height, Graphics.TOP | Graphics.HCENTER); 
        	g.setFont(smallBoldFont);
	        g.drawString(String.valueOf(station.number), width, height + mediumFontHeight + 1, Graphics.TOP | Graphics.HCENTER); 
	        
	        if (!station.open) {
	        	g.setColor(ColorUtil.WIDGET_DETAILS_ERROR);
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
        

        g.setColor(ColorUtil.WIDGET_DETAILS_BACKGROUND);
        g.fillRect(5, height, canvasWidth - 10, 5 + addressFont.getHeight() + 1 + smallFontHeight + 5);

        g.setColor(ColorUtil.WIDGET_DETAILS_FONT_MESSAGE);

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

        g.setColor(ColorUtil.WIDGET_DETAILS_BACKGROUND);
        g.fillRect(5, height + 5, canvasWidth - 10, 5 + (mediumFontHeight + 1) * 3 + 5);
        
        height += 5 + 2;
        
        g.setColor(ColorUtil.WIDGET_DETAILS_FONT_MESSAGE);
        g.setFont(mediumFont);
        int veloDispoWidth = mediumFont.stringWidth("Vélos disponibles: ") + 5 + mediumFont.stringWidth(String.valueOf("20"));
        
        int contentLeftPos = canvasWidth / 2 - veloDispoWidth / 2;
        int contentRightPos = canvasWidth / 2 + veloDispoWidth / 2;
        
        g.drawString("Vélos disponibles: ", contentLeftPos, height + 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString("20", contentRightPos - mediumFont.stringWidth("20"), height + 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("Places libres: ", contentLeftPos, height + mediumFontHeight + 1, Graphics.TOP | Graphics.LEFT); 
        g.drawString("10", contentRightPos - mediumFont.stringWidth("10"), height + mediumFontHeight + 1, Graphics.TOP | Graphics.LEFT);
        g.drawString("Hors service: ", contentLeftPos, height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT); 
        g.drawString("5", contentRightPos - mediumFont.stringWidth("5"), height + (mediumFontHeight + 1) * 2, Graphics.TOP | Graphics.LEFT);
	} 

}
