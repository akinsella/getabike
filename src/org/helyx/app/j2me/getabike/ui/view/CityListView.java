package org.helyx.app.j2me.getabike.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.renderer.text.DefaultTextRenderer;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class CityListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("CITY_LIST_VIEW");
	
	private City selectedCity;
	
	private boolean cancellable = false;
	
	private String country;
	
	private Vector cityList;

	public CityListView(AbstractMIDlet midlet, String country) {
		this(midlet, country, true);
	}
	
	public CityListView(AbstractMIDlet midlet, boolean cancellable) {
		this(midlet, null, cancellable);
	}
	
	public CityListView(AbstractMIDlet midlet, String country, boolean cancellable) {
		super(midlet, "", true);
		setTitle("view.city.title");
		this.country = country;
		this.cancellable = cancellable;
		init();
	}

	private void init() {
		setItemTextRenderer(new DefaultTextRenderer());
		initData();
		initComponents();
		initActions();
	}

	protected void initData() {
		if (country == null) {
			cityList = CityManager.findAllCities();
		}
		else {
			cityList = CityManager.findCitiesByCountryName(country);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("cityList: " + cityList);
		}
		
		selectedCity = CityManager.getCurrentCity();
		if (logger.isDebugEnabled()) {
			logger.debug("selectedCity: " + cityList);
		}
	}
	
	protected void initActions() {

		if (cancellable) {
			setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
	
				public void run(Object data) {
					fireReturnCallback();
				}
				
			}));
		}
		else {
			setSecondaryCommand(null);
		}
		
		setPrimaryCommand(new Command("command.select", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				if (menuItem != null) {
					City city = (City)menuItem.getData();
					CityManager.setCurrentCity(city);
					CartoManager.loadCityStations(CityListView.this, city);
				}
				else {
					fireReturnCallback();
				}
			}
			
		}));
	}

	protected void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = cityList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			MenuItem cityMenuItem = new MenuItem(city.name);
			cityMenuItem.setData(city);
			menu.addMenuItem(cityMenuItem);
			if (selectedCity != null && city.key.equals(selectedCity.key)) {
				menu.setCheckedMenuItem(cityMenuItem);
			}
		}

		setMenu(menu);
	}
}
