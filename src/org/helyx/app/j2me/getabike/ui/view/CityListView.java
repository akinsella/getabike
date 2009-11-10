package org.helyx.app.j2me.getabike.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.data.carto.listener.StoreStationLoaderProgressListener;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManager;
import org.helyx.app.j2me.getabike.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.manager.TaskManager;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.renderer.text.DefaultTextRenderer;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.ProgressTaskReturnCallback;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.menu.MenuListView;
import org.helyx.helyx4me.ui.widget.command.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
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
			progressTask.addProgressListener(new StoreStationLoaderProgressListener(progressTask.getProgressDispatcher(), CityListView.this));

			TaskManager.runLoadTaskView("view.city.station.update.title", progressTask, getMidlet(), CityListView.this, new ProgressTaskReturnCallback() {

				public void onSuccess(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
					if (logger.isDebugEnabled()) {
						logger.debug("Success: " + eventMessage + ", data: " + eventData);
					}
					CityManager.setCurrentCity(city);
					getReturnCallback().onReturn(currentDisplayable, eventData);
				}

				public void onError(final AbstractDisplayable currentDisplayable, final String eventMessage, final Object eventData) {
					if (logger.isInfoEnabled()) {
						logger.info("Error: " + eventMessage + ", data: " + eventData);
					}
					cleanUpCurrentCityData();
					
					Throwable t = (Throwable)eventData;

					
					DialogUtil.showMessageDialog(
							CityListView.this, 
							"dialog.title.error", 
							getMessage("connection.error") + ": " + ErrorManager.getErrorMessage(getMidlet(), t), 
							new OkResultCallback() {
								public void onOk(DialogView dialogView, Object data) {
									CityListView.this.getReturnCallback().onReturn(currentDisplayable, eventData);
								}
							});			

				}
				
			});
		}
		catch (CartoManagerException e) {
			logger.warn(e);
			cleanUpCurrentCityData();
			showAlertMessage(getMessage("dialog.title.error"), getMessage("view.city.load.error.1"));
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
