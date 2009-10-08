package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.velocite.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.task.StationLoadTask;
import org.helyx.app.j2me.velocite.data.city.accessor.ICityAcessor;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.ui.view.renderer.StationTitleRenderer;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.filter.IRecordFilter;
import org.helyx.helyx4me.map.google.POIInfoAccessor;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.ProgressListener;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.LoadTaskView;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.ImageSet;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class StationListView extends AbstractListView implements ICityAcessor {
	
	private static final Logger logger = Logger.getLogger("STATION_LIST_VIEW");
	
	private boolean recordFilterEnabled = true;
	
	private Station referentStation;
	
	private boolean allowMenu = true;
	private boolean allowNested = true;
	
	private City city;

	public StationListView(AbstractMIDlet midlet, String title) {
		super(midlet, title);
		setTitle(title);
		init();
	}
	
	private void init() {
		
		setTitleRenderer(new StationTitleRenderer(getMidlet(), this));
		initActions();
		initData();
		initComponents();
	}


	protected void initActions() {	
		
		if (allowMenu) {
			setThirdCommand(new Command("command.menu", true, getMidlet().getI18NTextRenderer(), new IAction() {
				public void run(Object data) {
					showMenuView();
				}
			}));
		}

		setPrimaryCommand(new Command("command.view", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				showItemSelected();
			}
		}));
	
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
			public void run(Object data) {
				fireReturnCallback();
			}
		}));

	}
	
	protected void showMenuView() {
		MenuListView menuListView = new MenuListView(getMidlet(), "view.station.list.menu.title", false);
		
		Menu menu = new Menu();
		
		menu.addMenuItem(new MenuItem("view.station.list.item.station.search", new ImageSet(getTheme().getString("IMG_FIND")), new IAction() {
			public void run(Object data) {
				searchStation();
			}
		}));
		
		if (city.localization && elementProvider.length() > 0) {
			menu.addMenuItem(new MenuItem("view.station.list.item.view.map", new ImageSet(getTheme().getString("IMG_MAP")), new IAction() {
				public void run(Object data) {
					showGoogleMapsView();
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
						DialogUtil.showAlertMessage(StationListView.this, "dialog.title.error", getMessage("view.station.list.item.view.website.error.message", t.getMessage()));
					}
				}
			}));
		}
		menuListView.setMenu(menu);
		menuListView.setPreviousDisplayable(StationListView.this);
		showDisplayable(menuListView, new BasicTransition());
	}


	private void showGoogleMapsView() {
		POIInfoAccessor poiInfoAccessor = new StationPoiInfoAccessor();
		Object elementSelected = getStationSelected();
		
		UtilManager.showGoogleMapsView(this, "view.station.list.map.title", poiInfoAccessor, elementSelected, elementProvider, 15);
	}
	
	protected Station getStationSelected() {
		return (Station)elementProvider.get(selectedOffset);
	}
	
	protected void showItemSelected() {
		onShowItemSelected(elementProvider.get(selectedOffset));
	}

	protected void initData() {
		// Nothing to do
	}

	protected void initComponents() {
		
	}
	
	protected void searchStation() {
		final String currentStationNameFilter = getStationNameFilter();
		StationSearchView stationSearchView = new StationSearchView(getMidlet());
		stationSearchView.setReturnCallback(new IReturnCallback() {

			public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
				String newStationNameFilter = getStationNameFilter();
				if ( (currentStationNameFilter == null  && newStationNameFilter != null ) ||
					 (currentStationNameFilter != null && !currentStationNameFilter.equals(newStationNameFilter)) ) {
					loadListContent(new UIStationLoaderProgressListener(StationListView.this));
				}
				else {
					currentDisplayable.showDisplayable(StationListView.this);
				}
			}
			
		});
		showDisplayable(stationSearchView);
	}
	
	protected String getStationNameFilter() {
		String stationNameFilter = PrefManager.readPrefString(StationSearchView.PREF_STATION_NAME_FILTER);
		logger.info("Station name filter: '" + stationNameFilter + "'");
		return stationNameFilter;
	}
	
	protected void onShowItemSelected(Object object) {
		showSelectedItem((Station)object);
	}

	protected void showSelectedItem(Station object) {
		Station station = (Station)object;
		
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), city, station);
		stationDetailsView.setAllowSearchNearStation(allowNested);
		stationDetailsView.setRelatedStations(elementProvider);

		showDisplayable(stationDetailsView, this);
	}

	public void loadListContent(ProgressListener progressListener) {
		
		IRecordFilter stationFilter = null;
		if (recordFilterEnabled) {
			String stationNameFilter = getStationNameFilter();
			if (stationNameFilter != null && stationNameFilter.length() > 0) {
				stationFilter = new StationNameFilter(stationNameFilter);
			}
		}

		final StationLoadTask stationLoadTask = new StationLoadTask(this, stationFilter);
		stationLoadTask.addProgressListener(progressListener);
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.station.list.load.station", stationLoadTask);
		showDisplayable(loadTaskView, this);
		resetPosition();
		loadTaskView.startTask();
		logger.info("Load List Content...");
	}

	public void setRecordFilterEnabled(boolean recordFilterEnabled) {
		this.recordFilterEnabled = recordFilterEnabled;
	}

	public Station getReferentStation() {
		return referentStation;
	}

	public void setReferentStation(Station referentStation) {
		this.referentStation = referentStation;
	}

	public boolean isAllowMenu() {
		return allowMenu;
	}

	public void setAllowMenu(boolean allowMenu) {
		this.allowMenu = allowMenu;
	}

	public boolean isAllowNested() {
		return allowNested;
	}

	public void setAllowNested(boolean allowNested) {
		this.allowNested = allowNested;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public City getCity() {
		return city;
	}

}
