package org.helyx.app.j2me.getabike.ui.view.station;

import org.helyx.app.j2me.getabike.data.carto.accessor.StationPoiInfoAccessor;
import org.helyx.app.j2me.getabike.data.carto.comparator.StationNameComparator;
import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.filter.BookmarkStationFilterBuilder;
import org.helyx.app.j2me.getabike.data.carto.task.StationLoadTask;
import org.helyx.app.j2me.getabike.data.city.accessor.ICityAcessor;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.contact.domain.Contact;
import org.helyx.app.j2me.getabike.data.contact.task.ContactLoadTask;
import org.helyx.app.j2me.getabike.ui.view.contact.ContactListView;
import org.helyx.app.j2me.getabike.ui.view.renderer.StationTitleRenderer;
import org.helyx.app.j2me.getabike.ui.view.station.search.StationSearchView;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.comparator.Comparator;
import org.helyx.helyx4me.filter.Filter;
import org.helyx.helyx4me.filter.FilterBuilder;
import org.helyx.helyx4me.localization.Point;
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
import org.helyx.helyx4me.ui.displayable.callback.BasicReturnCallback;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
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
	
	private Point location;
	
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
		if (getElementProvider().length() <= 0) {
			return null;
		}
		return (Station)getElementProvider().get(selectedOffset);
	}
	
	protected void showItemSelected() {
		if (getElementProvider().length() <= 0) {
			return ;
		}		
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
		
		if (station == null) {
			return;
		}
		
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), city, station);
		stationDetailsView.setAllowSearchNearStation(allowNested);
		stationDetailsView.setRelatedStations(refStationProvider.getElementProvider());
		stationDetailsView.setPreviousDisplayable(this);
		stationDetailsView.fetchStationDetails();
	}

	public void loadListContent() {
		StationLoadTask stationLoadTask = new StationLoadTask(this);
		
		final LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "view.station.list.load.station", stationLoadTask);
		loadTaskView.setReturnCallback(new BasicReturnCallback(this));
		stationLoadTask.addProgressListener(new ProgressAdapter("UI_CONTACT_PROGRESS_TAKS_LISTENER") {

			public void onSuccess(String eventMessage, Object eventData) {
				setStationsProvider(new ArrayElementProvider((Station[])eventData));
				
				StationListView.this.showDisplayable(StationListView.this, new BasicTransition());
			}
			
			public void onError(String eventMessage, Object eventData) {
				if (StationListView.logger.isInfoEnabled()) {
					StationListView.logger.info("Error: " + eventMessage + ", data: " + eventData);
				}
				
				Throwable t = (Throwable)eventData;

				DialogUtil.showMessageDialog(
						StationListView.this, 
						"dialog.title.error", 
						getMessage("dialog.title.error") + ": " + ErrorManager.getErrorMessage(getMidlet(), t), 
						new OkResultCallback() {
							public void onOk(DialogView dialogView, Object data) {
								loadTaskView.fireReturnCallback();
							}
						});
			}

		});

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

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
}
