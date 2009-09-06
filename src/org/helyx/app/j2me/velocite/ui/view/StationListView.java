package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.velocite.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.task.StationLoadTask;
import org.helyx.app.j2me.velocite.ui.theme.AppThemeConstants;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.filter.IRecordFilter;
import org.helyx.helyx4me.map.google.GoogleMapView;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.ProgressListener;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.LoadTaskView;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class StationListView extends AbstractListView {
	
	private static final Logger logger = Logger.getLogger("STATION_LIST_VIEW");
	
	private boolean recordFilterEnabled = true;
	
	private Station referentStation;
	
	private boolean allowMenu = true;
	private boolean allowNested = true;
	
	private String poiImgClasspath;
	private String poiSelectedImgClasspath;


	public StationListView(AbstractMIDlet midlet, String title) {
		super(midlet, title);
		init();
	}
	
	private void init() {
		initMapImagesPath();
		initActions();
		initData();
		initComponents();
	}
	
	private void initMapImagesPath() {
		try {
			poiImgClasspath = getTheme().getString(AppThemeConstants.MAP_POI_IMAGE_PATH);
			poiSelectedImgClasspath = getTheme().getString(AppThemeConstants.MAP_POI_SELECTED_IMAGE_PATH);
		}
		catch (Throwable t) {
			logger.warn(t);
		}
	}

	protected void initActions() {	
		
		if (allowMenu) {
			setThirdCommand(new Command("Menu", true, new IAction() {
				public void run(Object data) {
					showMenuView();
				}
			}));
		}

		setPrimaryCommand(new Command("Voir", true, new IAction() {
			public void run(Object data) {
				showItemSelected();
			}
		}));
	
		setSecondaryCommand(new Command("Retour", true, new IAction() {
			public void run(Object data) {
				fireReturnCallback();
			}
		}));

	}
	
	protected void showMenuView() {
		MenuListView menuListView = new MenuListView(getMidlet(), "Actions", false);
		
		Menu menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Chercher une station", new IAction() {
			public void run(Object data) {
				searchStation();
			}
		}));
		
		boolean mapModeEnabled = PrefManager.readPrefBoolean(UtilManager.MAP_MODE_ENABLED);

		if (mapModeEnabled && elementProvider.length() > 0) {
			menu.addMenuItem(new MenuItem("Voir le plan", new IAction() {
				
				public void run(Object data) {
					showGoogleMapsView();
				}
			}));
		}
		menuListView.setMenu(menu);
		menuListView.setPreviousDisplayable(StationListView.this);
		showDisplayable(menuListView, new BasicTransition());
	}


	private void showGoogleMapsView() {
		String googleMapsKey = PrefManager.readPrefString(UtilManager.GOOGLE_MAPS_KEY);
		
		Station stationSelected = getStationSelected();
		GoogleMapView googleMapView = new GoogleMapView(getMidlet(), "Plan de la station", googleMapsKey, new StationPoiInfoAccessor(), stationSelected.localization, 15, poiImgClasspath, poiSelectedImgClasspath);
		googleMapView.setPreviousDisplayable(StationListView.this);
		googleMapView.setSelectedPoi(stationSelected);
		googleMapView.setPoiItems(elementProvider);
		googleMapView.showDisplayable(googleMapView);
		googleMapView.updateMap();
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
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), station);
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
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "Chargement des stations", stationLoadTask);
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

}
