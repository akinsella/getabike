package org.helyx.app.j2me.getabike.ui.view.station.search;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.renderer.text.DefaultTextRenderer;
import org.helyx.app.j2me.getabike.lib.sort.FastQuickSort;
import org.helyx.app.j2me.getabike.lib.sort.StringComparator;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;

public class StationSearchCityFilterView extends MenuListView {

	private String[] cityNames;
	
	public StationSearchCityFilterView(AbstractMIDlet midlet, String[] cityNames) {
		super(midlet, "view.station.search.filter.city.tilte", true);
		FastQuickSort fastQuickSort = new FastQuickSort(new StringComparator());
		fastQuickSort.sort(cityNames);
		this.cityNames = cityNames;
		init();
	}
	
	private void init() {
		setItemTextRenderer(new DefaultTextRenderer());
		
		initComponents();
		initActions();
	}
	
	protected void initComponents() {
		Menu menu = new Menu();
		int zipCodesCount = cityNames.length;
		String currentZipCodeFilter = PrefManager.readPrefString(PrefConstants.PREF_STATION_CITY_FILTER);
		for (int i = 0 ; i < zipCodesCount ; i++) {
			String cityName = cityNames[i];
			MenuItem menuItem = new MenuItem(cityName);
			menuItem.setData(cityName);
			menu.addMenuItem(menuItem);
			if (cityName.equals(currentZipCodeFilter)) {
				menu.setCheckedMenuItem(menuItem);
			}
		}

		setMenu(menu);
	}
	
	protected void initActions() {
		
		setPrimaryCommand(new Command("command.select", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				if (menuItem != null) {
					String cityName = (String)menuItem.getData();
					PrefManager.writePref(PrefConstants.PREF_STATION_CITY_FILTER, cityName);
					PrefManager.removePref(PrefConstants.PREF_STATION_ZIPCODE_FILTER);
				}
				
				fireReturnCallback();
			}
			
		}));
		
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
			
			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setThirdCommand(new Command("command.clear", true, getMidlet().getI18NTextRenderer(), new IAction() {
			
			public void run(Object data) {
				PrefManager.removePref(PrefConstants.PREF_STATION_CITY_FILTER);
				fireReturnCallback();
			}
			
		}));

	}

}
