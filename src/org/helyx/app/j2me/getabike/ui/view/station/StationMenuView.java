package org.helyx.app.j2me.getabike.ui.view.station;

import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.pref.PrefBaseListView;
import org.helyx.helyx4me.ui.widget.ImageSet;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class StationMenuView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("STATION_MENU_VIEW");

	private MenuItem stationSearchMenuItem;
	private MenuItem showFavoritesMenuItem;
	
	private StationListView stationListView;

	public StationMenuView(AbstractMIDlet midlet, StationListView stationListView) {
		super(midlet, "");
		setTitle("view.station.list.menu.title");
		this.stationListView = stationListView;
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
		showFavoritesMenuItem.setData("PREF_VALUE", stationListView.isShowFavorites() ? getMessage("pref.yes") : getMessage("pref.no"));
	}
	
	protected void initComponents() {
		Menu menu = new Menu();
		
		stationSearchMenuItem = new MenuItem("view.station.list.item.station.search", new ImageSet(getTheme().getString("IMG_FIND")), new IAction() {
			public void run(Object data) {
				stationListView.configureStationSearchFilters();
			}
		});
		menu.addMenuItem(stationSearchMenuItem);
		
		showFavoritesMenuItem = new MenuItem("view.station.list.item.station.show.favorites", new ImageSet(getTheme().getString("IMG_STAR")), new IAction() {
			public void run(Object data) {
				stationListView.setShowFavorites(!stationListView.isShowFavorites());
				stationListView.filterAndSort();
			}
		});
		menu.addMenuItem(showFavoritesMenuItem);
		
		final City city = stationListView.getCity();
		
		if (logger.isInfoEnabled()) {
			logger.info("City: " + city);
		}
		
		if (city.localization && stationListView.isEmpty()) {
			menu.addMenuItem(new MenuItem("view.station.list.item.view.map", new ImageSet(getTheme().getString("IMG_MAP")), new IAction() {
				public void run(Object data) {
					stationListView.showGoogleMapsView();
				}
			}));
		}
		
		if (city != null && city.webSite != null) {
			menu.addMenuItem(new MenuItem("view.station.list.item.view.website", new ImageSet(getTheme().getString("IMG_WEB")), new IAction() {
				
				public void run(Object data) {
					if (logger.isInfoEnabled()) {
						logger.info("Open city ('" + city.name + "') web site URL: '" + city.webSite + "'");
					}
					try {
						getMidlet().platformRequest(city.webSite);
					}
					catch(Throwable t) {
						DialogUtil.showAlertMessage(stationListView, "dialog.title.error", getMessage("view.station.list.item.view.website.error.message", t.getMessage()));
					}
				}
			}));
		}
		
		setMenu(menu);
	}

}
