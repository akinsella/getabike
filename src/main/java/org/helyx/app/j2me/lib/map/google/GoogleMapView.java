package org.helyx.app.j2me.lib.map.google;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.concurrent.Mutex;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.DistanceUtil;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.view.support.list.IElementProvider;
import org.helyx.app.j2me.lib.ui.view.painter.ColorBackgroundZone;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Rectangle;

public class GoogleMapView extends AbstractView {

	private static final Log log = LogFactory.getLog("GOOGLE_MAP_VIEW");
	
	private GoogleMaps googleMaps = new GoogleMaps("ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA");
	private Image image;
	private Point localization;
	private int zoom = 15;
	private Mutex updateLock = new Mutex();
	private POIInfoAccessor poiInfoAccessor;
	private Rectangle screenMapRect;
	private IElementProvider poiItems;
	private Hashtable filteredPoiMap = new Hashtable();
	private boolean stopCurrentPoiFiltering = true;
	
	private Image poiImg;
	private Image poiImgSelected;
	
	private Object poiSelected;
	private Point poiSelectedLoc;
	private org.helyx.app.j2me.lib.ui.geometry.Point poiSelectedPoint;
	
	public GoogleMapView(AbstractMIDlet midlet, String title, POIInfoAccessor poiInfoAccessor) {
		super(midlet);
		setTitle(title);
		this.poiInfoAccessor = poiInfoAccessor;
		init();
	}
	
	public GoogleMapView(AbstractMIDlet midlet, String title, POIInfoAccessor poiInfoAccessor, Point localization, int zoom) {
		super(midlet);
		setTitle(title);
		this.poiInfoAccessor = poiInfoAccessor;
		this.localization = localization;
		this.zoom = zoom;
		init();
		updateMap();
	}
	 
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	protected void initComponents() {
		setFullScreenMode(true);
		setBackgroundZone(new ColorBackgroundZone(ColorUtil.WHITE));

		try {
			poiImg = ImageUtil.createImageFromClassPath("/org/helyx/app/j2me/velocite/image/vcPointer.png");
			poiImgSelected = ImageUtil.createImageFromClassPath("/org/helyx/app/j2me/velocite/image/vcPointerSelected.png");
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
	}

	protected void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		log.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
		
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();

		if (gameAction == GameCanvas.LEFT) {
			log.info("--- X: " + 2 * width / 3);
	    	setLocalization(googleMaps.adjust(localization, width / 4, 0, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			log.info("--- X: " + -(2 * width / 3));
	    	setLocalization(googleMaps.adjust(localization, -width / 4, 0, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.UP) {
			log.info("--- Y: " + -(height / 2));
	    	setLocalization(googleMaps.adjust(localization, 0, -height / 4, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.DOWN) {
			log.info("--- Y: " + (height / 2));
	    	setLocalization(googleMaps.adjust(localization, 0, height / 4, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.FIRE) {
			zoomIn();
		}
	    else if (gameAction == GameCanvas.KEY_NUM3) {
			zoomOut();
		}
	    else if (gameAction == GameCanvas.GAME_A) {
			zoomIn();
		}
	    else if (gameAction == GameCanvas.GAME_B) {
			zoomOut();
		}
	}

	protected void initActions() {
		
		setThirdCommand(new Command("Menu", true, new IAction() {

			public void run(Object data) {

				final MenuListView menuListView = new MenuListView(getMidlet(), "Menu", false);

				Menu menu = new Menu();
				
				menu.addMenuItem(new MenuItem("Zoom In", new IAction() {				
					public void run(Object data) {
						zoomIn();
						menuListView.fireReturnCallback();
					}
				}));
				menu.addMenuItem(new MenuItem("Zoom Out", new IAction() {				
					public void run(Object data) {
						zoomOut();
						menuListView.fireReturnCallback();
					}
				}));
				
				menuListView.setMenu(menu);
				menuListView.setPreviousDisplayable(GoogleMapView.this);

				showDisplayable(menuListView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
	}
	
	private void zoomIn() {
		setZoom(zoom < 17 ? zoom + 1 : zoom);
		updateMap();
	}
	
	private void zoomOut() {
		setZoom(zoom > 10 ? zoom - 1 : zoom);
		updateMap();
	}

	protected void initData() {

	}
	
	protected void paint(Graphics graphics) {
		
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();

		if (image != null) {
			graphics.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
		}
		
		else {
			graphics.setColor(ColorUtil.LIGHT_GREY);
			for (int i = 25 ; i < width ; i += 25) {
				graphics.drawLine(i, 0, i, height);
				for (int j = 25 ; j < height ; j += 25) {
					graphics.drawLine(0, j, width, j);					
				}
			}
			
		}

		Enumeration _enum = filteredPoiMap.keys();
//		log.info("Iterating points ...");
		while (_enum.hasMoreElements()) {
			org.helyx.app.j2me.lib.ui.geometry.Point point = (org.helyx.app.j2me.lib.ui.geometry.Point)_enum.nextElement();
//			log.info("Point: " + point);
			
			if (poiImg != null) {
				graphics.drawImage(poiImg, point.x - poiImg.getWidth() / 2, point.y - poiImg.getHeight(), Graphics.TOP | Graphics.LEFT);
			}
		}
		
		if (poiSelected != null && poiImgSelected != null) {
			graphics.drawImage(poiImgSelected, poiSelectedPoint.x - poiImgSelected.getWidth() / 2, poiSelectedPoint.y - poiImgSelected.getHeight(), Graphics.TOP | Graphics.LEFT);
		}

	}

	public void setLocalization(Point localization) {
		this.localization = localization;
	}
	
	private void updateScreenRectMap() {
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();

		screenMapRect = new Rectangle(
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lng) + ((width / 2) << (21 - zoom))),
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lat) + ((height / 2) << (21 - zoom))),
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lng) - ((width / 2) << (21 - zoom))),
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lat) - ((height / 2) << (21 - zoom)))
		);	
		log.info("screenRectMap: " + screenMapRect);
		
		updatePoiSelectedPoint();
	}

	public boolean updateMap() {
		
		if (localization == null || !updateLock.tryLock()) {
			return false;
		}

		stopCurrentPoiFiltering = true;
		filteredPoiMap.clear();

		updateScreenRectMap();

		new Thread() {
			public void run() {
				try {
					updateImage();
				}
				finally {
					updateLock.unLock();
					repaint();
				}
				updatePoi();
			}
		}.start();
		
		return true;
	}
	
	private void updateImage() {
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();
		try {
			image = googleMaps.retrieveStaticImage(width, height, localization.lat, localization.lng, zoom, "png32");		
		}
		catch(Throwable t) {
			DialogUtil.showAlertMessage(GoogleMapView.this, "Erreur", t.getMessage() != null ? t.getMessage() : "Impossible de charge l'image");
			image = null;
		}
	}

	public Point getLocalization() {
		return localization;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
		log.info("Zoom: " + zoom);
	}
	
	public void updatePoi() {
		log.info("Updating Poi ...");
		Thread updatePoiThread = new Thread() {
			public void run() {
				int height = viewCanvas.getHeight();
				stopCurrentPoiFiltering = false;
				filteredPoiMap.clear();
				if (poiItems == null) {
					return;
				}
				int length = poiItems.length();
				log.info("Poi Items: " + length);
				for (int i = 0 ; i < length ; i++) {
					if (stopCurrentPoiFiltering) {
						break;
					}
					Object poiItem = poiItems.get(i);

					Point poiLoc = poiInfoAccessor.getLocalization(poiItem);
//					log.info("poiLoc: " + poiLoc + ", screenMapRect: " + screenMapRect);
					if ( poiLoc.lng >= screenMapRect.min.lng &&
						 poiLoc.lng <= screenMapRect.max.lng &&
						 poiLoc.lat >= screenMapRect.min.lat &&
						 poiLoc.lat <= screenMapRect.max.lat ) {

//						log.info("poiLoc: " + poiLoc + ", screenMapRect: " + screenMapRect);

						int x = ((int)(DistanceUtil.LToX(screenMapRect.min.lng) - DistanceUtil.LToX(poiLoc.lng)) >> (21 - zoom));
						int y = height - ((int)(DistanceUtil.LToY(screenMapRect.min.lat) - DistanceUtil.LToY(poiLoc.lat)) >> (21 - zoom));

						filteredPoiMap.put(new org.helyx.app.j2me.lib.ui.geometry.Point(x, y), poiItem);
						repaint();
					}
				}
			}
		};
		
		updatePoiThread.start();

	}

	public IElementProvider getPoiItems() {
		return poiItems;
	}

	public void setPoiItems(IElementProvider poiItems) {
		this.poiItems = poiItems;
	}

	public Object getSelectedPoi() {
		return poiSelected;
	}

	public void setSelectedPoi(Object poiSelected) {
		this.poiSelected = poiSelected;
		poiSelectedLoc = poiInfoAccessor.getLocalization(poiSelected);
		updatePoiSelectedPoint();
	}
	
	private void updatePoiSelectedPoint() {
		if (poiSelected != null) {
			
			int x = ((int)(DistanceUtil.LToX(screenMapRect.min.lng) - DistanceUtil.LToX(poiSelectedLoc.lng)) >> (21 - zoom));
			int y = viewCanvas.getHeight() - ((int)(DistanceUtil.LToY(screenMapRect.min.lat) - DistanceUtil.LToY(poiSelectedLoc.lat)) >> (21 - zoom));

			poiSelectedPoint = new org.helyx.app.j2me.lib.ui.geometry.Point(x, y);
			
//			log.info("poiSelectedPoint: " + poiSelectedPoint);
		}
	}

}
