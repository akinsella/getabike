package org.helyx.app.j2me.getabike.ui.view.station.search;

import java.util.Vector;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.ui.view.station.StationListView;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.PrefBaseListView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class StationSearchView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("PREF_LIST_VIEW");
	
	private StationListView stationListView;

	private MenuItem nameFilterMenuItem;
	private MenuItem zipCodeFilterMenuItem;
	private MenuItem cityFilterMenuItem;

	public StationSearchView(AbstractMIDlet midlet, StationListView stationListView) {
		super(midlet, "");
		setTitle("view.pref.title");
		this.stationListView = stationListView;
		init();
	}
	 
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	private Vector getZipCodeList() {
		Station[] stations = stationListView.getAllStations();
		Vector zipCodeList = new Vector();
		int stationCount = stations.length;
		for (int i = 0 ; i < stationCount ; i++) {
			Station station = stations[i];
			if (station.zipCode != null && station.zipCode.length() > 0 && !zipCodeList.contains(station.zipCode)) {
				zipCodeList.addElement(station.zipCode);
			}
		}
		
		return zipCodeList;
	}

	protected void initComponents() {
		
		Menu menu = new Menu();
		
		nameFilterMenuItem = new MenuItem("view.station.search.item.name", new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				StationSearchNameFilterView stationNameSearchView = new StationSearchNameFilterView(currentView.getMidlet());
				currentView.showDisplayable(stationNameSearchView, currentView);
			}
		});
		
		zipCodeFilterMenuItem = new MenuItem("view.station.search.item.zipcode", new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				Vector zipCodeList = getZipCodeList();
				if (zipCodeList.isEmpty()) {
					DialogUtil.showAlertMessage(StationSearchView.this, "dialog.title.information", "view.station.search.no.zip.code");
				}
				else {
					StationSearchZipCodeFilterView stationZipCodeSearchView = new StationSearchZipCodeFilterView(currentView.getMidlet(), zipCodeList);
					currentView.showDisplayable(stationZipCodeSearchView, currentView);
				}
			}
		});
		
		cityFilterMenuItem = new MenuItem("view.station.search.item.city", new IAction() {
			public void run(Object data) {
				AbstractView currentView = StationSearchView.this;
				DialogUtil.showAlertMessage(currentView, "dialog.title.alert", getMessage("view.station.search.not.yet.implemented"));
			}
		});
		
		menu.addMenuItem(nameFilterMenuItem);
		menu.addMenuItem(zipCodeFilterMenuItem);
		menu.addMenuItem(cityFilterMenuItem);
		
		setMenu(menu);
	}

	protected void initData() {

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
