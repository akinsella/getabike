package org.helyx.app.j2me.velocite.view;

import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.IDisplayableReturnCallback;
import org.helyx.app.j2me.lib.ui.view.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManager;
import org.helyx.app.j2me.velocite.data.carto.manager.CartoManagerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;

public class PrefListView extends MenuListView {

	private static final String CAT = "PREF_LIST_VIEW";
	
	public PrefListView(MIDlet midlet, IDisplayableReturnCallback displayableReturnCallback) {
		super(midlet, false, displayableReturnCallback);
		init();
	}

	public PrefListView(MIDlet midlet) {
		super(midlet, false);
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("Préférences");
		
		Menu menu = new Menu();
		
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Sélectionner une ville", new IAction() {
			public void run(Object data) {
				CityListView cityListView;
				try {
					cityListView = new CityListView(getMidlet());
					cityListView.setPreviousDisplayable(PrefListView.this);
					showDisplayable(cityListView);
				}
				catch (CityManagerException e) {
					Log.warn(CAT, e);
					showAlertMessage("Problème de configuration", "Le fichier des villes n'est pas valide: " + e.getMessage());
				}
			}
		}));
		
		menu.addMenuItem(new MenuItem("Rafraîchir les stations", new IAction() {
			public void run(Object data) {

				try {
					City selectedCity = CityManager.findSelectedCity();
					CartoManager.refreshAll(selectedCity, getMidlet(), PrefListView.this, PrefListView.this);
				}
				catch (CartoManagerException e) {
					showAlertMessage("Erreur", e.getMessage() != null ? e.getMessage() : "StationManagerException");
				}
				catch(CityManagerException e) {
					Log.warn(CAT, e);
					showAlertMessage("Problème de configuration", "Le fichier des villes n'est pas valide: " + e.getMessage());
				}
			}
		}));

		setMenu(menu);
	}


}
