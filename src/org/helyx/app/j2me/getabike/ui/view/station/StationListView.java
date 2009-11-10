package org.helyx.app.j2me.getabike.ui.view.station;

import org.helyx.app.j2me.getabike.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.getabike.data.carto.comparator.StationNameComparator;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.filter.BookmarkStationFilterBuilder;
import org.helyx.app.j2me.getabike.data.carto.task.StationLoadTask;
import org.helyx.app.j2me.getabike.data.city.accessor.ICityAcessor;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.ui.view.renderer.StationTitleRenderer;
import org.helyx.app.j2me.getabike.ui.view.station.search.StationSearchView;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.helyx4me.filter.Filter;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.helyx4me.map.google.POIInfoAccessor;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.model.list.IDynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.IElementProvider;
import org.helyx.helyx4me.model.list.impl.ArrayElementProvider;
import org.helyx.helyx4me.model.list.impl.DynamicFilterableSortableElementProvider;
import org.helyx.helyx4me.model.list.impl.RefElementProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.list.AbstractListView;
import org.helyx.helyx4me.ui.view.support.task.LoadTaskView;
import org.helyx.helyx4me.ui.view.transition.BasicTransition;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class StationListView extends AbstractListView implements ICityAcessor {
	
	private static final Logger logger = Logger.getLogger("STATION_LIST_VIEW");
	
	private RefElementProvider refStationProvider;
	private IDynamicFilterableSortableElementProvider filteredSortedElementProvider;
	
	private Station referentStation;
	
	private boolean allowMenu = true;
	private boolean allowNested = true;
	private boolean showBookmarks = false;
	
	public boolean isShowBookmarks() {
		return showBookmarks;
	}

	public void setShowBookmarks(boolean showBookmarks) {
		this.showBookmarks = showBookmarks;
	}

	private FilterBuilder filterBuilder;
	
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
		StationListMenuView stationMenuView = new StationListMenuView(getMidlet(), this);
		stationMenuView.setPreviousDisplayable(StationListView.this);
		showDisplayable(stationMenuView, new BasicTransition());	
	}


	void showGoogleMapsView() {
		POIInfoAccessor poiInfoAccessor = new StationPoiInfoAccessor();
		Object stationSelected = getStationSelected();
		
		UtilManager.showGoogleMapsView(this, "view.station.list.map.title", poiInfoAccessor, stationSelected, getElementProvider(), 15);
	}
	
	protected Station getStationSelected() {
		return (Station)getElementProvider().get(selectedOffset);
	}
	
	protected void showItemSelected() {
		onShowItemSelected(getElementProvider().get(selectedOffset));
	}

	protected void initData() {
		IElementProvider arrayStationsProvider = new ArrayElementProvider(new Station[0]);
		refStationProvider = new RefElementProvider(arrayStationsProvider);
		filteredSortedElementProvider = new DynamicFilterableSortableElementProvider(refStationProvider);
		filteredSortedElementProvider.setComparator(new StationNameComparator());
		setItems((IElementProvider)filteredSortedElementProvider);
	}

	protected void initComponents() {
		
	}
	
	public Station[] getAllStations() {
		return (Station[])refStationProvider.getElementProvider().getElements();
	}
	
	protected void configureStationSearchFilters() {
		StationSearchView stationSearchView = new StationSearchView(getMidlet(), this);
		stationSearchView.setReturnCallback(new IReturnCallback() {

			public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
				filterAndSort();
				
				showDisplayable(StationListView.this);
			}
			
		});
		showDisplayable(stationSearchView);
	}
	
	public void filterAndSort() {
		Filter filter = showBookmarks ? new BookmarkStationFilterBuilder(city).buildFilter() : (filterBuilder != null ? filterBuilder.buildFilter() : null);
		
		resetPosition();
		filteredSortedElementProvider.setFilter(filter);
		filteredSortedElementProvider.filterAndSort();
	}

	protected void onShowItemSelected(Object object) {
		showSelectedItem((Station)object);
	}

	protected void showSelectedItem(Station object) {
		Station station = (Station)object;
		
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), city, station);
		stationDetailsView.setAllowSearchNearStation(allowNested);
		stationDetailsView.setRelatedStations(refStationProvider.getElementProvider());
		stationDetailsView.fetchStationDetails();

//		showDisplayable(stationDetailsView, this);
	}

	public void loadListContent() {
		StationLoadTask stationLoadTask = new StationLoadTask(this);
		stationLoadTask.addProgressListener(new ProgressAdapter("UI_PROGRESS_TAKS_LISTENER") {
			
			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				StationListView stationListView = StationListView.this;
				switch (eventType) {
					case EventType.ON_SUCCESS:
						setStationsProvider(new ArrayElementProvider((Station[])eventData));
						
						stationListView.showDisplayable(stationListView, new BasicTransition());

						break;

					case EventType.ON_ERROR:
						Throwable throwable = (Throwable)eventData;
						getLogger().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
						DialogUtil.showAlertMessage(stationListView, "dialog.title.error", stationListView.getMessage("dialog.error.unexpected") + ": " + throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
						stationListView.showDisplayable(stationListView, new BasicTransition());
						break;
						
					default:
						DialogUtil.showAlertMessage(stationListView, "dialog.title.error", stationListView.getMessage("dialog.result.unexpected"));
						stationListView.showDisplayable(stationListView, new BasicTransition());
						break;
				}
			}
		});
		
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.station.list.load.station", stationLoadTask);
		showDisplayable(loadTaskView, this);
		resetPosition();
		logger.info("Load List Content...");
		loadTaskView.startTask();
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

	public void setComparator(Comparator comparator) {
		filteredSortedElementProvider.setComparator(comparator);
	}

	public void setStationsProvider(IElementProvider stationsProvider) {
		refStationProvider.setElementProvider(stationsProvider);
		filterAndSort();
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	public boolean isEmpty() {
		return getElementProvider().length() <= 0;
	}
	
}
