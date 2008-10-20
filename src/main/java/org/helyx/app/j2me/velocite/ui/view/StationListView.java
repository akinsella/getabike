package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.task.ProgressListener;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.DialogUtil;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.view.support.LoadTaskView;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.view.support.list.AbstractListView;
import org.helyx.app.j2me.lib.ui.view.support.list.ArrayElementProvider;
import org.helyx.app.j2me.lib.ui.view.support.list.IElementProvider;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;
import org.helyx.app.j2me.velocite.data.carto.filter.StationNameFilter;
import org.helyx.app.j2me.velocite.task.StationLoadTask;

public class StationListView extends AbstractListView {
	
	private static final Log log = LogFactory.getLog("STATION_LIST_VIEW");
	
	private Menu menu;
	private MenuListView prefMenuListView;

	public StationListView(AbstractMIDlet midlet) {
		super(midlet, "Liste des stations");
		init();
		initActions();
	}
	
	private void init() {
		initActions();
		initData();
		initComponents();
	}
	
	protected void initActions() {
		
//		setPrimaryCommand(new Command("Voir", true, new IAction() {
//
//			public void run(Object data) {
//				showCurrentStationSelected();
//			}
//			
//		}));
//
//		setSecondaryCommand(new Command("Retour", true, new IAction() {
//
//			public void run(Object data) {
//				returnToPreviousDisplayable();
//			}
//			
//		}));
		
		setThirdCommand(new Command("Menu", true, new IAction() {

			public void run(Object data) {
				prefMenuListView = new MenuListView(getMidlet(), "Actions", false);
				prefMenuListView.setMenu(menu);
				prefMenuListView.setPreviousDisplayable(StationListView.this);
				showDisplayable(prefMenuListView, new BasicTransition());
			}
			
		}));
	}

	protected void initComponents() {
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Chercher une station", new IAction() {
			public void run(Object data) {
				showDisplayable(new StationSearchView(getMidlet(), new IReturnCallback() {

					public void onReturn(Object data) {
						loadListContent();
					}
					
				}), StationListView.this);
			}
		}));
		
	}

	protected void initData() {
		loadListContent();
	}
	
	private String getStationNameFilter() {
		String stationName = PrefManager.readPrefString(StationSearchView.PREF_STATION_NAME);
		
		return stationName;
	}
	
	protected void onShowItemSelected(Object object) {
		Station station = (Station)object;
		showDisplayable(new StationDetailsView(getMidlet(), station), this);
	}

	public void loadListContent() {
		
		IRecordFilter stationFilter = null;
		String stationNameFilter = getStationNameFilter();
		if (stationNameFilter != null && stationNameFilter.length() > 0) {
			stationFilter = new StationNameFilter(stationNameFilter);
		}

		final StationLoadTask stationLoadTask = new StationLoadTask(this, stationFilter);
		stationLoadTask.addProgressListener(loadStationListProgressListener);
		LoadTaskView loadTaskView = new LoadTaskView(getMidlet(), "Chargement des stations", stationLoadTask);
		showDisplayable(loadTaskView, this);
		selectedOffset = 0;
		topOffset = 0;
		loadTaskView.startTask();
		log.info("Load List Content...");
	}

	public void setStations(Station[] stationArray) {
		IElementProvider elementProvider = new ArrayElementProvider(stationArray != null ? stationArray : new Station[0]);
		setElementProvider(elementProvider);
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
	
	private ProgressListener loadStationListProgressListener = new ProgressAdapter() {

		public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
			switch (eventType) {
				case ProgressEventType.ON_SUCCESS:
					showDisplayable(StationListView.this, new BasicTransition());
					break;
					
				case ProgressEventType.ON_CANCEL:
					showDisplayable(StationListView.this, new BasicTransition());
					break;
	
				case ProgressEventType.ON_ERROR:
					Throwable throwable = (Throwable)eventData;
					getLog().warn(throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
					DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), "Erreur", throwable.getMessage() == null ? throwable.toString() : throwable.getMessage());
					showDisplayable(StationListView.this, new BasicTransition());
					break;
					
				default:
					break;
			}
		}
		
	};

}
