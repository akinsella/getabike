package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.ProgressListener;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.LoadTaskView;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.lib.ui.view.support.list.IFilterableElementProvider;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.velocite.data.carto.listener.UIStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.task.StationLoadTask;

public class StationListView extends AbstractListView {
	
	private static final Log log = LogFactory.getLog("STATION_LIST_VIEW");
	
	private Menu menu;
	
	private boolean recordFilterEnabled = true;
	
	private Station referentStation;
	
	private boolean allowMenu = true;
	private boolean allowNested = true;

	public StationListView(AbstractMIDlet midlet, String title) {
		super(midlet, title);
		init();
	}
	
	private void init() {
		initActions();
		initData();
		initComponents();
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
		menuListView.setMenu(menu);
		menuListView.setPreviousDisplayable(StationListView.this);
		showDisplayable(menuListView, new BasicTransition());
	}
	
	protected void showItemSelected() {
		onShowItemSelected(elementProvider.get(selectedOffset));
	}

	protected void initData() {
		// Nothing to do
	}

	protected void initComponents() {
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Chercher une station", new IAction() {
			public void run(Object data) {
				searchStation();
			}
		}));
		
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
		log.info("Station name filter: '" + stationNameFilter + "'");
		return stationNameFilter;
	}
	
	protected void onShowItemSelected(Object object) {
		showSelectedItem((Station)object);
	}

	protected void showSelectedItem(Station object) {
		Station station = (Station)object;
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), station);
		stationDetailsView.setAllowSearchNearStation(allowNested);
		stationDetailsView.setRelatedStations(elementProvider instanceof IFilterableElementProvider ? ((IFilterableElementProvider)elementProvider).getUnfilteredElements() : elementProvider);

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
		log.info("Load List Content...");
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
