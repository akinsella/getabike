package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.listener.StoreStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;

public class CityListView extends MenuListView {
	
	private static final Log log = LogFactory.getLog("CITY_LIST_VIEW");
	
	private City selectedCity;
	
	private Vector cityList;
	
	public CityListView(AbstractMIDlet midlet) throws CityManagerException {
		super(midlet, "Choix de la ville", true);
		init();
	}
	
	private void init() {
		initData();
		initComponents();
		initActions();
	}
	
	protected void initActions() {

		setSecondaryCommand(new Command("Annuler", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
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
					progressTask.addProgressListener(new StoreStationLoaderProgressListener(progressTask.getProgressDispatcher()));

					TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, getMidlet(), CityListView.this, getReturnCallback());

				}
				catch (CartoManagerException e) {
					showAlertMessage("Erreur", e.getMessage() != null ? e.getMessage() : "CityManagerException");
					fireReturnCallback();
				}
			}
			
		}));
	}

	protected void initData() {
		cityList = CityManager.findAllCities();
		log.info("cityList: " + cityList);
		selectedCity = CityManager.findSelectedCity(cityList);
		log.info("selectedCity: " + cityList);
	}
	
	protected void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = cityList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			if (city.active) {
				MenuItem cityMenuItem = new MenuItem(city.name);
				cityMenuItem.setData(city);
				menu.addMenuItem(cityMenuItem);
				if (selectedCity != null && city.key.equals(selectedCity.key)) {
					menu.setCheckedMenuItem(cityMenuItem);
				}
			}
		}

		setMenu(menu);
	}
}
