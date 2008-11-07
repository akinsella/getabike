package org.helyx.app.j2me.lib.map.google;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.concurrent.Mutex;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.util.ColorUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.view.painter.ColorBackgroundZone;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class GoogleMapView extends AbstractView {

	private static final Log log = LogFactory.getLog("GOOGLE_MAP_VIEW");
	
	private GoogleMaps googleMaps = new GoogleMaps("ABQIAAAAm-1Nv9nWhCjQxC3a4v-lBBR9EQxSSSz8gi3qW-McXAXnYGN8YxSBOridiu5I3THzW-_0oY2DecnShA");
	private Image image;
	private Point localization;
	private int zoom = 15;
	Mutex updateLock = new Mutex();
	
	public GoogleMapView(AbstractMIDlet midlet, String title) {
		super(midlet);
		setTitle(title);
		init();
	}
	 
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	protected void initComponents() {
		setFullScreenMode(true);
		setBackgroundZone(new ColorBackgroundZone(ColorUtil.WHITE));
	}

	protected void onKeyReleased(int keyCode) {
		// TODO Auto-generated method stub
	}

	protected void initActions() {
		
		setThirdCommand(new Command("Menu", true, new IAction() {

			public void run(Object data) {

				final MenuListView menuListView = new MenuListView(getMidlet(), "Menu", false);

				Menu menu = new Menu();
				
				menu.addMenuItem(new MenuItem("Zoom In", new IAction() {				
					public void run(Object data) {
						setZoom(zoom < 19 ? zoom + 1 : zoom);
						updateMap();
						menuListView.fireReturnCallback();
					}
				}));
				menu.addMenuItem(new MenuItem("Zoom Out", new IAction() {				
					public void run(Object data) {
						setZoom(zoom > 1 ? zoom - 1 : zoom);
						updateMap();
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

	protected void initData() {
		updateMap();
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
	}

	public void setLocalization(Point localization) {
		this.localization = localization;
	}
	
	public boolean updateMap() {
		if (localization != null && !updateLock.tryLock()) {
			return false;
		}
		new Thread() {
			public void run() {
				try {
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
				finally {
					updateLock.unLock();
					repaint();
				}
			}
		}.start();
		
		return true;
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
}
