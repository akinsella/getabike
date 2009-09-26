package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.helyx4me.ui.widget.ImageSet;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class CountryListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("COUNTRY_LIST_VIEW");
	
	private boolean cancellable = false;
	
	private Vector countryList;
	
	private String currentCountry;

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
		countryList = CityManager.findAllCountries();
		currentCountry = CityManager.getCurrentCountry();
		logger.info("countryList: " + countryList);
		logger.info("currentCountry: " + currentCountry);
	}
	
	protected void initActions() {

		if (cancellable) {
			setSecondaryCommand(new Command("Retour", true, new IAction() {
	
				public void run(Object data) {
					fireReturnCallback();
				}
				
			}));
		}
		
		setPrimaryCommand(new Command("Sélectionner", true, new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				final String country = (String)menuItem.getData();
				
				if (country != null && !country.equals(currentCountry)) {
					CityManager.setCurrentCountry(country);
					CityManager.clearCurrentCity(true);
				}
				
				fireReturnCallback();
			}
			
		}));
	}
	
	protected void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = countryList.elements();
		while(_enum.hasMoreElements()) {
			String country = (String)_enum.nextElement();
			MenuItem countryMenuItem = new MenuItem(getMessage("velocite.country." + country));
			try {
				countryMenuItem.setImageSet(new ImageSet(getTheme().getString("velocite.country." + country + ".flag")));
			}
			catch(Throwable t) { logger.warn(t); }
			countryMenuItem.setData(country);
			if (currentCountry != null && country.equals(currentCountry)) {
				menu.setCheckedMenuItem(countryMenuItem);
			}
			menu.addMenuItem(countryMenuItem);
		}

		setMenu(menu);
	}
}
