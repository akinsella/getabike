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
package org.helyx.app.j2me.getabike.lib.map.google;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.map.google.GoogleMapsView;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.cache.Cache;
import org.helyx.app.j2me.getabike.lib.concurrent.Mutex;
import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.localization.Rectangle;
import org.helyx.app.j2me.getabike.lib.map.google.GoogleMaps;
import org.helyx.app.j2me.getabike.lib.map.google.POIInfoAccessor;
import org.helyx.app.j2me.getabike.lib.math.DistanceUtil;
import org.helyx.app.j2me.getabike.lib.model.list.IElementProvider;
import org.helyx.app.j2me.getabike.lib.thread.StoppableThread;
import org.helyx.app.j2me.getabike.lib.ui.geometry.PointDbl;
import org.helyx.app.j2me.getabike.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.painter.background.ColorBackgroundPainter;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class GoogleMapsView extends AbstractView {

	private static final Logger logger = Logger.getLogger("GOOGLE_MAP_VIEW");
	
	private static final int ZOOM_MAX = 21;
	
	private GoogleMaps googleMaps;
	private Image googleMapsImage;
	private Point localization;
	private int zoom = 15;
	private Mutex updateLock = new Mutex();
	private POIInfoAccessor poiInfoAccessor;
	private Rectangle screenMapRect;
	private IElementProvider poiItems;
	private Hashtable filteredPoiMap = new Hashtable();
	
	private Image poiImg;
	private Image poiImgSelected;
	
	private Object poiSelected;
	private Point poiSelectedLoc;
	private org.helyx.app.j2me.getabike.lib.ui.geometry.Point poiSelectedPoint;
	
	private String poiImgClasspath = "/org/helyx/helyx4me/map/google/poi.png";
	private String poiSelectedImgClasspath = "/org/helyx/helyx4me/map/google/poiSelected.png";
	private Cache cache = new Cache();
	
	private String googleMapsKey = "DUMMY_GOOGLE_MAPS_KEY";
	
	private StoppableThread updatePoiThread;
	
	public GoogleMapsView(AbstractMIDlet midlet, String title, String googleMapsKey, POIInfoAccessor poiInfoAccessor) {
		super(midlet);
		setTitle(title);
		this.googleMapsKey = googleMapsKey;
		this.poiInfoAccessor = poiInfoAccessor;
		init();
	}
	
	public GoogleMapsView(AbstractMIDlet midlet, String title, String googleMapsKey, POIInfoAccessor poiInfoAccessor, String poiImgClasspath, String poiSelectedImgClasspath) {
		super(midlet);
		setTitle(title);
		this.googleMapsKey = googleMapsKey;
		this.poiInfoAccessor = poiInfoAccessor;
		this.poiImgClasspath = poiImgClasspath;
		this.poiSelectedImgClasspath = poiSelectedImgClasspath;
		init();
	}
	
	public GoogleMapsView(AbstractMIDlet midlet, String title, String googleMapsKey, POIInfoAccessor poiInfoAccessor, Point localization, int zoom) {
		super(midlet);
		setTitle(title);
		this.googleMapsKey = googleMapsKey;
		this.poiInfoAccessor = poiInfoAccessor;
		this.localization = localization;
		this.zoom = zoom;
		init();
	}
	
	public GoogleMapsView(AbstractMIDlet midlet, String title, String googleMapsKey, POIInfoAccessor poiInfoAccessor, Point localization, int zoom, String poiImgClasspath, String poiSelectedImgClasspath) {
		super(midlet);
		setTitle(title);
		this.googleMapsKey = googleMapsKey;
		this.poiInfoAccessor = poiInfoAccessor;
		this.localization = localization;
		this.zoom = zoom;
		this.poiImgClasspath = poiImgClasspath;
		this.poiSelectedImgClasspath = poiSelectedImgClasspath;
		init();
	}
	 
	private void init() {
		googleMaps = new GoogleMaps(googleMapsKey);
		setScreenDragging(true);
		initActions();
		initData();
		initComponents();
		updateMap();
	}

	protected void initComponents() {
		setFullScreenMode(true);
		setClientBackgroundPainter(new ColorBackgroundPainter(ColorUtil.WHITE));

		try {
			poiImg = ImageUtil.createImageFromClassPath(poiImgClasspath);
			poiImgSelected = ImageUtil.createImageFromClassPath(poiSelectedImgClasspath);
		} catch (IOException e) {
			logger.warn(e.getMessage());
		}
	}

	public void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
		if (logger.isDebugEnabled()) {
			logger.debug("[onKeyPressed] gameAction: " + gameAction + ", keyCode: " + keyCode);
		}
		
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();

		if (gameAction == GameCanvas.LEFT) {
			if (logger.isDebugEnabled()) {
				logger.debug("--- X: " + 2 * width / 3);
			}
	    	setLocalization(googleMaps.adjust(localization, width / 4, 0, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.RIGHT) {
			if (logger.isDebugEnabled()) {
				logger.debug("--- X: " + -(2 * width / 3));
			}
	    	setLocalization(googleMaps.adjust(localization, -width / 4, 0, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.UP) {
			if (logger.isDebugEnabled()) {
				logger.debug("--- Y: " + -(height / 2));
			}
	    	setLocalization(googleMaps.adjust(localization, 0, -height / 4, zoom));
			updateMap();
		}
	    else if (gameAction == GameCanvas.DOWN) {
			if (logger.isDebugEnabled()) {
				logger.debug("--- Y: " + (height / 2));
			}
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
		
		setThirdCommand(new Command("command.menu", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {

				final MenuListView menuListView = new MenuListView(getMidlet(), "view.google.maps.command.menu", false);

				Menu menu = new Menu();
				
				String zoomInImgPath = getTheme().getString("IMG_ZOOM_IN");
				String zoomOutImgPath = getTheme().getString("IMG_ZOOM_OUT");
				
				menu.addMenuItem(new MenuItem("view.google.maps.item.zoom.in", 
						zoomInImgPath != null ? new ImageSet(zoomInImgPath) : null,
						new IAction() {				
					public void run(Object data) {
						zoomIn();
						menuListView.fireReturnCallback();
					}
				}));
				menu.addMenuItem(new MenuItem("view.google.maps.item.zoom.out", 
						zoomOutImgPath != null ? new ImageSet(zoomOutImgPath) : null,
						new IAction() {				
					public void run(Object data) {
						zoomOut();
						menuListView.fireReturnCallback();
					}
				}));
				
				menuListView.setMenu(menu);
				menuListView.setPreviousDisplayable(GoogleMapsView.this);

				showDisplayable(menuListView, new BasicTransition());
			}
			
		}));
		
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {

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
		setZoom(zoom > 9 ? zoom - 1 : zoom);
		updateMap();
	}

	protected void initData() {

	}
	
	protected void paint(Graphics graphics) {
		
		int width = viewCanvas.getWidth();
		int height = viewCanvas.getHeight();

		if (googleMapsImage != null) {
			graphics.drawImage(googleMapsImage, 0, 0, Graphics.TOP | Graphics.LEFT);
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
		int poiImgWidth = 0;
		int poiImgHeight = 0;
		if (poiImg != null) {
			poiImgWidth = poiImg.getWidth();
			poiImgHeight = poiImg.getHeight();
		}
		
//		logger.info("Iterating points ...");
		while (_enum.hasMoreElements()) {
			org.helyx.app.j2me.getabike.lib.ui.geometry.Point point = (org.helyx.app.j2me.getabike.lib.ui.geometry.Point)_enum.nextElement();
//			logger.info("Point: " + point);
			
			if (poiImg != null) {
				graphics.drawImage(poiImg, point.x - poiImgWidth / 2, point.y - poiImgHeight, Graphics.TOP | Graphics.LEFT);
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
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lng) + ((width / 2) << (ZOOM_MAX - zoom))),
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lat) + ((height / 2) << (ZOOM_MAX - zoom))),
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lng) - ((width / 2) << (ZOOM_MAX - zoom))),
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lat) - ((height / 2) << (ZOOM_MAX - zoom)))
		);	
		if (logger.isDebugEnabled()) {
			logger.debug("screenRectMap: " + screenMapRect);
		}
		
		updatePoiSelectedPoint();
	}

	public boolean updateMap() {
		if (localization == null || !updateLock.tryLock()) {
			return false;
		}
		googleMapsImage = null;

		if (updatePoiThread != null) {
			updatePoiThread.askToStop(true);
		}
				
		updateScreenRectMap();

		updatePoi();

		updateGoogleMapsImageAndUnlock();
		
		return true;
	}
	
	private void updateGoogleMapsImageAndUnlock() {

		final int width = viewCanvas.getWidth();
		final int height = viewCanvas.getHeight();
		
		new Thread() {
			public void run() {
				try {
//					double x = DistanceUtil.LToX(localization.lng);
//					double y = DistanceUtil.LToY(localization.lat);
//
//					org.helyx.app.j2me.getabike.lib.ui.geometry.Point positionPoint = new org.helyx.app.j2me.getabike.lib.ui.geometry.Point((int)x , (int)y);
//					
//					double lng = DistanceUtil.XToL(positionPoint.x);
//					double lat = DistanceUtil.YToL(positionPoint.y);
//					Point roundedLocalization = new Point(lng, lat);
//					
					googleMapsImage = googleMaps.retrieveStaticImage(width, height, localization.lat, localization.lng, zoom, "png32");		
				}
				catch(Throwable t) {
					DialogUtil.showAlertMessage(GoogleMapsView.this, "dialog.title.error", t.getMessage() != null ? t.getMessage() : getMessage("view.google.maps.image.load.error"));
					googleMapsImage = null;
				}
				finally {
					updateLock.unLock();
					repaint();
				}
			}
		}.start();
	}

	public Point getLocalization() {
		return localization;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
		logger.info("Zoom: " + zoom);
	}
	
	public void clearCache() {
		cache.clear();
	}
	
	public void updatePoi() {
		if (logger.isInfoEnabled()) {
			logger.info("Updating Poi ...");
		}
		filteredPoiMap.clear();
		final int height = viewCanvas.getHeight();
		
		updatePoiThread = new StoppableThread() {
			public void run() {
				if (poiItems == null) {
					return;
				}
				int length = poiItems.length();
				if (logger.isInfoEnabled()) {
					logger.info("Poi Item count: " + length);
				}
				double distanceLtoXScreenMapRectMinLng = DistanceUtil.LToX(screenMapRect.min.lng);
				double distanceLtoYScreenMapRectMinLat = DistanceUtil.LToY(screenMapRect.min.lat);
				
				int poiOnScreenCount = 0;
				long currentTime = System.currentTimeMillis();
				for (int i = 0 ; i < length ; i++) {
					if (shouldStop()) {
						return ;
					}
					Object poiItem = poiItems.get(i);

					Point poiLoc = poiInfoAccessor.getLocalization(poiItem);
//					logger.info("poiLoc: " + poiLoc + ", screenMapRect: " + screenMapRect);
					if ( poiLoc.lng >= screenMapRect.min.lng &&
						 poiLoc.lng <= screenMapRect.max.lng &&
						 poiLoc.lat >= screenMapRect.min.lat &&
						 poiLoc.lat <= screenMapRect.max.lat ) {
						poiOnScreenCount++;
//						logger.info("poiLoc: " + poiLoc + ", screenMapRect: " + screenMapRect);
						PointDbl poiLocPosition = null;
						if (!cache.containsKey(poiLoc)) {
							poiLocPosition = new PointDbl(
									DistanceUtil.LToX(poiLoc.lng), 
									DistanceUtil.LToY(poiLoc.lat));
							cache.set(poiLoc, poiLocPosition);
						}
						else {
							poiLocPosition = (PointDbl)cache.get(poiLoc);
						}
						int x = ((int)(distanceLtoXScreenMapRectMinLng - poiLocPosition.x) >> (ZOOM_MAX - zoom));
						int y = height - ((int)(distanceLtoYScreenMapRectMinLat - poiLocPosition.y) >> (ZOOM_MAX - zoom));

						filteredPoiMap.put(new org.helyx.app.j2me.getabike.lib.ui.geometry.Point(x, y), poiItem);
						
						long newTime = System.currentTimeMillis();
						long timeDelta = (currentTime - newTime) / 1000;
						if (timeDelta > 0) {
							currentTime = newTime;
							repaint();
						}
					}
				}
				repaint();
			}
		};
		
		updatePoiThread.start();
	}

	public IElementProvider getPoiItems() {
		return poiItems;
	}

	public void setPoiItems(IElementProvider poiItems) {
		this.poiItems = poiItems;
		updatePoi();
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
			
			int x = ((int)(DistanceUtil.LToX(screenMapRect.min.lng) - DistanceUtil.LToX(poiSelectedLoc.lng)) >> (ZOOM_MAX - zoom));
			int y = viewCanvas.getHeight() - ((int)(DistanceUtil.LToY(screenMapRect.min.lat) - DistanceUtil.LToY(poiSelectedLoc.lat)) >> (ZOOM_MAX - zoom));

			poiSelectedPoint = new org.helyx.app.j2me.getabike.lib.ui.geometry.Point(x, y);
			
//			logger.info("poiSelectedPoint: " + poiSelectedPoint);
		}
	}

}
