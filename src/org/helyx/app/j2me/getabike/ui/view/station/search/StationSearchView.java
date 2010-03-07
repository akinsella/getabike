package org.helyx.app.j2me.getabike.ui.view.station.search;

import java.util.Vector;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.pref.PrefBaseListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class StationSearchView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("STATION_SEARCH_VIEW");
	
	private StationListView stationListView;

	private MenuItem nameFilterMenuItem;
	private MenuItem zipCodeFilterMenuItem;
	private MenuItem cityFilterMenuItem;

	public StationSearchView(AbstractMIDlet midlet, StationListView stationListView) {
		super(midlet, "");
		setTitle("view.station.search.title");
		this.stationListView = stationListView;
		init();
	}
	 
	private void init() {
		initActions();
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

	private String[] getZipCodes() {
		Station[] stations = stationListView.getAllStations();
		Vector zipCodeList = new Vector();
		int stationCount = stations.length;
		for (int i = 0 ; i < stationCount ; i++) {
			Station station = stations[i];
			if (station.zipCode != null && station.zipCode.length() > 0 && !zipCodeList.contains(station.zipCode)) {
				zipCodeList.addElement(station.zipCode);
			}
		}
		
		String[] zipCodes = new String[zipCodeList.size()];
		zipCodeList.copyInto(zipCodes);

		return zipCodes;
	}

	private String[] getCityNames() {
		Station[] stations = stationListView.getAllStations();
		Vector cityNameList = new Vector();
		int stationCount = stations.length;
		for (int i = 0 ; i < stationCount ; i++) {
			Station station = stations[i];
			if (station.city != null && station.city.length() > 0 && !cityNameList.contains(station.city)) {
				cityNameList.addElement(station.city);
			}
		}
		
		String[] cityNames = new String[cityNameList.size()];
		cityNameList.copyInto(cityNames);

		return cityNames;
	}

	protected void initComponents() {
		
		Menu menu = new Menu();
		
		nameFilterMenuItem = new MenuItem("view.station.search.item.name", new ImageSet(getTheme().getString("IMG_FILTER")), new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				StationSearchNameFilterView stationNameSearchView = new StationSearchNameFilterView(currentView.getMidlet());
				stationNameSearchView.setReturnCallback(getReturnCallback());
				currentView.showDisplayable(stationNameSearchView);
			}
		});
		
		zipCodeFilterMenuItem = new MenuItem("view.station.search.item.zipcode", new ImageSet(getTheme().getString("IMG_FILTER")), new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				String[] zipCodes = getZipCodes();
				if (zipCodes.length <= 1) {
					DialogUtil.showAlertMessage(StationSearchView.this, "dialog.title.information", getMessage("view.station.search.no.zip.code"));
				}
				else {
					StationSearchZipCodeFilterView stationZipCodeSearchView = new StationSearchZipCodeFilterView(currentView.getMidlet(), zipCodes);
					stationZipCodeSearchView.setReturnCallback(getReturnCallback());
					currentView.showDisplayable(stationZipCodeSearchView);
				}
			}
		});
		
		cityFilterMenuItem = new MenuItem("view.station.search.item.city", new ImageSet(getTheme().getString("IMG_FILTER")), new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				String[] cityName = getCityNames();
				if (cityName.length <= 1) {
					DialogUtil.showAlertMessage(StationSearchView.this, "dialog.title.information", getMessage("view.station.search.no.city"));
				}
				else {
					StationSearchCityFilterView stationSearchCityFilterView = new StationSearchCityFilterView(currentView.getMidlet(), cityName);
					stationSearchCityFilterView.setReturnCallback(getReturnCallback());
					currentView.showDisplayable(stationSearchCityFilterView);
				}
			}
		});
		
		menu.addMenuItem(nameFilterMenuItem);
		menu.addMenuItem(zipCodeFilterMenuItem);
		menu.addMenuItem(cityFilterMenuItem);
		
		setMenu(menu);
	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		if (logger.isDebugEnabled()) {
			logger.debug("Current: " + current);
			logger.debug("Next: " + next);
		}
		if (next == this) {
			fetchNameFilter();
			fetchZipCodeFilter();
			fetchCityFilter();
		}
		super.beforeDisplayableSelection(current, next);
	}

	private void fetchNameFilter() {
		try {
			nameFilterMenuItem.setData(PREF_VALUE, PrefManager.readPrefString(PrefConstants.PREF_STATION_NAME_FILTER, ""));
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}

	private void fetchZipCodeFilter() {
		try {
			zipCodeFilterMenuItem.setData(PREF_VALUE, PrefManager.readPrefString(PrefConstants.PREF_STATION_ZIPCODE_FILTER, ""));
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}

	private void fetchCityFilter() {
		try {
			cityFilterMenuItem.setData(PREF_VALUE, PrefManager.readPrefString(PrefConstants.PREF_STATION_CITY_FILTER, ""));
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}

}
