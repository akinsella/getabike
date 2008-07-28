package org.helyx.app.j2me.velocite.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.IDisplayableReturnCallback;
import org.helyx.app.j2me.lib.ui.view.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;

public class CityListView extends MenuListView {
	
	private static final String CAT = "CITY_LIST_VIEW";
	
	private City selectedCity;
	
	private Vector cityList;

	public CityListView(AbstractMIDlet midlet, IDisplayableReturnCallback displayableReturnCallback) throws CityManagerException {
		super(midlet, true, displayableReturnCallback);
		init();
	}

	public CityListView(AbstractMIDlet midlet) throws CityManagerException {
		super(midlet, true);
		init();
	}
	
	private void init() throws CityManagerException {
		setFullScreenMode(true);
		setTitle("Choix de la ville");
		initData();
		initComponents();
		initActions();
	}
	
	private void initActions() {

		setSecondaryAction(new Command("Retour", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
	}

	private void initData() throws CityManagerException {
		cityList = CityManager.findAllCities();
		Log.info(CAT, "cityList: " + cityList);
		selectedCity = CityManager.findSelectedCity(cityList);
		Log.info(CAT, "selectedCity: " + cityList);
	}
	
	private void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = cityList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			if (city.active) {
				MenuItem cityMenuItem = new MenuItem(city.name);
				cityMenuItem.setData(city);
				menu.addMenuItem(cityMenuItem);
				if (city.key.equals(selectedCity.key)) {
					menu.setCheckedMenuItem(cityMenuItem);
				}
			}
		}

		setMenu(menu);
	}

	public void returnToPreviousDisplayable() {
		MenuItem menuItem = getMenu().getCheckedMenuItem();
		
		Log.info(CAT, "City choose menu return: " + menuItem);
		
		City city = (City)menuItem.getData();

		if (!city.key.equals(selectedCity.key)) {
			CityManager.saveSelectedCity(city);
			try {
				CartoManager.refreshAll(city, getMidlet(), this, getPreviousDisplayable());
			}
			catch (CartoManagerException e) {
				showAlertMessage("Erreur", e.getMessage() != null ? e.getMessage() : "StationManagerException");
				super.returnToPreviousDisplayable();
			}
		}
		else {
			super.returnToPreviousDisplayable();
		}

	}
}
