package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.listener.StationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;

public class CityListView extends MenuListView {
	
	private static final String CAT = "CITY_LIST_VIEW";
	
	private City selectedCity;
	
	private Vector cityList;

	public CityListView(AbstractMIDlet midlet, IReturnCallback returnCallback) throws CityManagerException {
		super(midlet, true, returnCallback);
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

		setSecondaryCommand(new Command("Annuler", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
		
		setPrimaryCommand(new Command("Ok", true, new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				City city = (City)menuItem.getData();
				CityManager.saveSelectedCity(city);
				try {
					IProgressTask progressTask = CartoManager.refreshAll(city);
					progressTask.addProgressListener(new StationLoaderProgressListener(progressTask.getProgressDispatcher()));

					TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, getMidlet(), CityListView.this, getPreviousDisplayable());

				}
				catch (CartoManagerException e) {
					showAlertMessage("Erreur", e.getMessage() != null ? e.getMessage() : "CityManagerException");
					returnToPreviousDisplayable();
				}
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
}
