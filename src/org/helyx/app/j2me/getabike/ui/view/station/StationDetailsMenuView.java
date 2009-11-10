package org.helyx.app.j2me.getabike.ui.view.station;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.view.support.menu.MenuListView;
import org.helyx.helyx4me.ui.view.support.pref.PrefBaseListView;
import org.helyx.helyx4me.ui.widget.ImageSet;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class StationDetailsMenuView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("STATION_DETAILS_MENU_VIEW");

	private StationDetailsView stationDetailsView;

	public StationDetailsMenuView(AbstractMIDlet midlet, StationDetailsView stationDetailsView) {
		super(midlet, "");
		setTitle("view.station.detail.menu.title");
		this.stationDetailsView = stationDetailsView;
		init();
	}
	
	private void init() {
		initComponents();
		initData();
		initCommands();
	}
	
	private void initCommands() {
		
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				fireReturnCallback();
			}
		}));

	}
	
	protected void initData() {

	}
	
	protected void initComponents() {
		Menu menu = new Menu();
		final City city = stationDetailsView.getCity();
		
		if (city.localization) {
			menu.addMenuItem(new MenuItem("view.station.detail.menu.item.map", new ImageSet(getTheme().getString("IMG_MAP")), new IAction() {
				
				public void run(Object data) {
					stationDetailsView.showGoogleMapsView();
				}
			}));
		}
		
		if (stationDetailsView.isAllowSearchNearStation() && city.localization) {
			menu.addMenuItem(new MenuItem("view.station.detail.menu.item.near.station", new ImageSet(getTheme().getString("IMG_NEAR")), new IAction() {
				
				public void run(Object data) {
					final MenuListView nearStationMenuListView = new MenuListView(getMidlet(), "view.station.detail.item.near.station.menu.title", false);

					Menu nearStationMenu = new Menu();
					nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.250", new IAction() {
						
						public void run(Object data) {
							CartoManager.showStationByDistance(nearStationMenuListView, city, stationDetailsView.getRelatedStations(), stationDetailsView.getStation(), 250, false, false);
						}
	
					}));
					nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.500", new IAction() {
						
						public void run(Object data) {
							CartoManager.showStationByDistance(nearStationMenuListView, city, stationDetailsView.getRelatedStations(), stationDetailsView.getStation(), 500, false, false);
						}
	
					}));
					nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.1000", new IAction() {
						
						public void run(Object data) {
							CartoManager.showStationByDistance(nearStationMenuListView, city, stationDetailsView.getRelatedStations(), stationDetailsView.getStation(), 1000, false, false);
						}
	
					}));
					nearStationMenu.addMenuItem(new MenuItem("view.station.detail.item.near.station.menu.2000", new IAction() {
						
						public void run(Object data) {
							CartoManager.showStationByDistance(nearStationMenuListView, city, stationDetailsView.getRelatedStations(), stationDetailsView.getStation(), 2000, false, false);
						}
	
					}));
					
					nearStationMenuListView.setMenu(nearStationMenu);
					nearStationMenuListView.setPreviousDisplayable(stationDetailsView);
					showDisplayable(nearStationMenuListView);
				}

			}));
		}
		
		menu.addMenuItem(new MenuItem(CartoManager.isStationNumberBookmarked(city, stationDetailsView.getStation()) ? "view.station.detail.menu.item.bookmark.remove" : "view.station.detail.menu.item.bookmark.add", new ImageSet(getTheme().getString("IMG_STAR")), new IAction() {
			
			public void run(Object data) {
				boolean isStataionBookmarked = CartoManager.isStationNumberBookmarked(city, stationDetailsView.getStation());
				if (isStataionBookmarked) {
					CartoManager.removeStationNumberFromBookmarks(city, stationDetailsView.getStation());
				}
				else {
					CartoManager.addStationNumberToBookmarks(city, stationDetailsView.getStation());
				}
				fireReturnCallback();
			}

		}));

		setMenu(menu);
	}

}
