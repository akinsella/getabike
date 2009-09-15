package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.data.carto.listener.StoreStationLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.manager.TaskManager;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.ProgressTaskReturnCallback;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class CityListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("CITY_LIST_VIEW");
	
	private City selectedCity;
	
	private boolean cancellable = false;
	
	private String country;
	
	private Vector cityList;

	public CityListView(AbstractMIDlet midlet, String country) throws CityManagerException {
		this(midlet, country, true);
	}
	
	public CityListView(AbstractMIDlet midlet, boolean cancellable) throws CityManagerException {
		this(midlet, null, cancellable);
	}
	
	public CityListView(AbstractMIDlet midlet, String country, boolean cancellable) throws CityManagerException {
		super(midlet, "Choix de la ville", true);
		this.country = country;
		this.cancellable = cancellable;
		init();
	}

	private void init() {
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
		logger.info("cityList: " + cityList);
		
		selectedCity = CityManager.getCurrentCity();
		logger.info("selectedCity: " + cityList);
	}
	
	protected void initActions() {

		if (cancellable) {
			setSecondaryCommand(new Command("Annuler", true, new IAction() {
	
				public void run(Object data) {
					fireReturnCallback();
				}
				
			}));
		}
		
		setPrimaryCommand(new Command("Ok", true, new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				if (menuItem != null) {
					City city = (City)menuItem.getData();
					CityManager.setCurrentCity(city);
					loadCityStations(city);
				}
				else {
					fireReturnCallback();
				}
			}
			
		}));
	}
	
	protected void loadCityStations(final City city) {
		
		try {
			IProgressTask progressTask = CartoManager.createUpdateCityStationsTask(city);
			progressTask.addProgressListener(new StoreStationLoaderProgressListener(progressTask.getProgressDispatcher()));

			TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, getMidlet(), CityListView.this, new ProgressTaskReturnCallback() {

				public void onError(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
					logger.info("Error: " + eventMessage + ", data: " + eventData);
					cleanUpCurrentCityData();
					getReturnCallback().onReturn(currentDisplayable, eventData);
				}

				public void onSuccess(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
					logger.info("Success: " + eventMessage + ", data: " + eventData);
					CityManager.setCurrentCity(city);
					getReturnCallback().onReturn(currentDisplayable, eventData);
				}
				
			});
		}
		catch (CartoManagerException e) {
			logger.warn(e);
			cleanUpCurrentCityData();
			showAlertMessage("Erreur", "Impossible de charger les stations pour la ville sélectionnée.");
			fireReturnCallback();
		}
	}
	
	private void cleanUpCurrentCityData() {
		try {
			CityManager.clearCurrentCity(true);
		}
		catch(Throwable t) {
			logger.warn(t);
		}
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
