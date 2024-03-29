package org.helyx.app.j2me.getabike.ui.view.station;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.pref.PrefBaseListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class StationListMenuView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("STATION_LIST_MENU_VIEW");

	private MenuItem stationSearchMenuItem;
	
	private StationListView stationListView;

	public StationListMenuView(AbstractMIDlet midlet, StationListView stationListView) {
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
	}
	
	protected void initComponents() {
		Menu menu = new Menu();

		if (!stationListView.isShowBookmarks()) {
			stationSearchMenuItem = new MenuItem("view.station.list.item.station.search", new ImageSet(getTheme().getString("IMG_FIND")), new IAction() {
				public void run(Object data) {
					stationListView.configureStationSearchFilters();
				}
			});
			menu.addMenuItem(stationSearchMenuItem);
		}
		
		final City city = stationListView.getCity();
		
		if (city.localization && !stationListView.isEmpty()) {
			menu.addMenuItem(new MenuItem("view.station.list.item.view.map", new ImageSet(getTheme().getString("IMG_MAP")), new IAction() {
				public void run(Object data) {
					stationListView.showGoogleMapsView();
				}
			}));
		}
		
		if (city != null && city.webSite != null) {
			menu.addMenuItem(new MenuItem("view.station.list.item.view.website", new ImageSet(getTheme().getString("IMG_WEB")), new IAction() {
				
				public void run(Object data) {
					if (logger.isDebugEnabled()) {
						logger.debug("Open city ('" + city.name + "') web site URL: '" + city.webSite + "'");
					}
					try { getMidlet().platformRequest(city.webSite); }
					catch(Throwable t) {
						DialogUtil.showAlertMessage(stationListView, "dialog.title.error", getMessage("view.station.list.item.view.website.error.message", t.getMessage()));
					}
				}
			}));
		}

		menu.addMenuItem(new MenuItem(CartoManager.isStationNumberBookmarked(city, stationListView.getStationSelected()) ? "view.station.detail.menu.item.bookmark.remove" : "view.station.detail.menu.item.bookmark.add", new ImageSet(getTheme().getString("IMG_STAR")), new IAction() {
			
			public void run(Object data) {
				boolean isStataionBookmarked = CartoManager.isStationNumberBookmarked(city, stationListView.getStationSelected());
				if (isStataionBookmarked) {
					CartoManager.removeStationNumberFromBookmarks(city, stationListView.getStationSelected());
				}
				else {
					CartoManager.addStationNumberToBookmarks(city, stationListView.getStationSelected());
				}
				fireReturnCallback();
			}

		}));

		
		setMenu(menu);
	}

}
