package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.ProgressListener;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
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
	private boolean nestedView = false;

	public StationListView(AbstractMIDlet midlet, String title, boolean nestedView) {
		super(midlet, title);
		this.nestedView = nestedView;
		init();
	}
	
	private void init() {
		initActions();
		initData();
		initComponents();
	}
	
	protected void initActions() {
		if (!nestedView) {		
			setThirdCommand(new Command("Menu", true, new IAction() {
	
				public void run(Object data) {
					MenuListView menuListView = new MenuListView(getMidlet(), "Actions", false);
					menuListView.setMenu(menu);
					menuListView.setPreviousDisplayable(StationListView.this);
					showDisplayable(menuListView, new BasicTransition());
				}
				
			}));
		}

		setPrimaryCommand(new Command("Voir", true, new IAction() {

			public void run(Object data) {
				onShowItemSelected(elementProvider.get(selectedOffset));
			}
			
		}));
	
		setSecondaryCommand(new Command("Retour", true, new IAction() {
	
			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));

	}

	protected void initData() {
		// Nothing to do
	}

	protected void initComponents() {
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Chercher une station", new IAction() {
			public void run(Object data) {
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
		}));
		
	}
	
	private String getStationNameFilter() {
		String stationNameFilter = PrefManager.readPrefString(StationSearchView.PREF_STATION_NAME_FILTER);
		log.info("Station name filter: '" + stationNameFilter + "'");
		return stationNameFilter;
	}
	
	protected void onShowItemSelected(Object object) {
		showSelectedItem((Station)object);
	}

	private void showSelectedItem(Station object) {
		Station station = (Station)object;
		StationDetailsView stationDetailsView = new StationDetailsView(getMidlet(), station, nestedView);
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

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {

		Station station = (Station)itemObject;
    	
    	boolean isSelected = isItemSelected(offset);

    	super.paintItem(g, offset, itemClientArea, itemObject);
     	if (isSelected) {
    		Color listFontSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SELECTED);
    		g.setColor(listFontSelectedColor.intValue());
    	}
    	else {
    		Color listFontColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT);
    		g.setColor(listFontColor.intValue());
    	}
        if (!station.open) {
    		Color listErrorColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_ERROR);
         	g.setColor(listErrorColor.intValue());
        }
        g.setFont(FontUtil.SMALL_BOLD);
        g.drawString(station.number + " - " + station.name, itemClientArea.location.x + 5, itemClientArea.location.y + 2, Graphics.LEFT | Graphics.TOP);
 
    	if (isSelected) {
    		Color listFontSecondSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND_SELECTED);
    		g.setColor(listFontSecondSelectedColor.intValue());
    	}
    	else {
    		Color listFontSecondColor = getTheme().getColor(ThemeConstants.WIDGET_LIST_FONT_SECOND);
    		g.setColor(listFontSecondColor.intValue());
    	}
        g.setFont(FontUtil.SMALL);
    	g.drawString(station.fullAddress, itemClientArea.location.x + 5, itemClientArea.location.y + 2 + FontUtil.SMALL_BOLD.getHeight(), Graphics.LEFT | Graphics.TOP);
	}

	public void setRecordFilterEnabled(boolean recordFilterEnabled) {
		this.recordFilterEnabled = recordFilterEnabled;
	}

	public boolean isNestedView() {
		return nestedView;
	}

}
