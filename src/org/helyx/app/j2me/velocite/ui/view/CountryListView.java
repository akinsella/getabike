package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.data.app.manager.VeloCiteManager;
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

public class CountryListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("COUNTRY_LIST_VIEW");
	
	private boolean cancellable = false;
	
	private Vector countryList;

	public CountryListView(AbstractMIDlet midlet) throws CityManagerException {
		this(midlet, true);
	}
	
	public CountryListView(AbstractMIDlet midlet, boolean cancellable) throws CityManagerException {
		super(midlet, "Choix du pays", true);
		this.cancellable = cancellable;
		init();
	}
	
	private void init() {
		initData();
		initComponents();
		initActions();
	}

	protected void initData() {
		countryList = CityManager.findAllCities();
		logger.info("countryList: " + countryList);
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
				
				final City city = (City)menuItem.getData();
				
				try {
					IProgressTask progressTask = CartoManager.createUpdateCityStationsTask(city);
					progressTask.addProgressListener(new StoreStationLoaderProgressListener(progressTask.getProgressDispatcher()));

					TaskManager.runLoadTaskView("Mise à jour des stations", progressTask, getMidlet(), CountryListView.this, new ProgressTaskReturnCallback() {

						public void onError(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
							logger.info("Error: " + eventMessage + ", data: " + eventData);
							VeloCiteManager.cleanUpCitySelectedData();
							getReturnCallback().onReturn(currentDisplayable, eventData);
						}

						public void onSuccess(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
							logger.info("Success: " + eventMessage + ", data: " + eventData);
							CityManager.saveSelectedCity(city);
							getReturnCallback().onReturn(currentDisplayable, eventData);
						}
						
					});
				}
				catch (CartoManagerException e) {
					logger.warn(e);
					showAlertMessage("Erreur", "Impossible de charger les stations pour la ville sélectionnée.");
					fireReturnCallback();
				}
			}
			
		}));
	}
	
	protected void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = countryList.elements();
		while(_enum.hasMoreElements()) {
			City city = (City)_enum.nextElement();
			if (city.active) {
				MenuItem cityMenuItem = new MenuItem(city.name);
				cityMenuItem.setData(city);
				menu.addMenuItem(cityMenuItem);
				if (selectedCountry != null && city.key.equals(selectedCountry.key)) {
					menu.setCheckedMenuItem(cityMenuItem);
				}
			}
		}

		setMenu(menu);
	}
}
