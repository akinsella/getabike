package org.helyx.app.j2me.velocite.view;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;

public class PrefListView extends MenuListView {

	private static final String CAT = "PREF_LIST_VIEW";
	
	public PrefListView(AbstractMIDlet midlet, IReturnCallback displayableReturnCallback) {
		super(midlet, false, displayableReturnCallback);
		init();
	}

	public PrefListView(AbstractMIDlet midlet) {
		super(midlet, false);
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("Préférences");
		
		Menu menu = new Menu();
		
		menu = new Menu();
		
		menu.addMenuItem(new MenuItem("Villes", new IAction() {
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
		
		menu.addMenuItem(new MenuItem("Langues", new IAction() {
			public void run(Object data) {
				LanguageListView languageListView;
				try {
					languageListView = new LanguageListView(getMidlet());
					languageListView.setPreviousDisplayable(PrefListView.this);
					showDisplayable(languageListView);
				}
				catch (LanguageManagerException e) {
					Log.warn(CAT, e);
					showAlertMessage("Problème de configuration", "Le fichier des langues n'est pas valide: " + e.getMessage());
				}
			}
		}));

		setMenu(menu);
	}


}
