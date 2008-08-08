package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.PrefBaseListView;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;

public class PrefListView extends PrefBaseListView {

	private static final String CAT = "PREF_LIST_VIEW";
	
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;

	public PrefListView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}
	
	public PrefListView(AbstractMIDlet midlet, IReturnCallback displayableReturnCallback) {
		super(midlet, displayableReturnCallback);
		init();
	}
	
	private void init() {
		setFullScreenMode(true);
		setTitle("Préférences");
		
		Menu menu = new Menu();
		
		cityMenuItem = new MenuItem("Villes", new IAction() {
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
		});
		
		languageMenuItem = new MenuItem("Langues", new IAction() {
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
		});
		
		
		menu.addMenuItem(cityMenuItem);
		menu.addMenuItem(languageMenuItem);
		
		setMenu(menu);
	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		if (next == this) {
			fetchCityPref();
			fetchLanguagePref();
		}
		super.beforeDisplayableSelection(current, next);
	}

	private void fetchCityPref() {
		try {
			String cityKey = PrefManager.readPrefValue(PrefConstants.CITY_SELECTED_KEY);
			if (cityKey != null && !cityKey.equals(languageMenuItem.getData("city.key"))) {
				cityMenuItem.setData("city.key", cityKey);
				City city = CityManager.findSelectedCity();
				cityMenuItem.setData(PREF_VALUE, city.name);
			}
		}
		catch (CityManagerException e) {
			Log.debug(CAT, e);
		}
	}

	private void fetchLanguagePref() {
		try {
			String languageKey = PrefManager.readPrefValue(PrefConstants.LANGUAGE_SELECTED_KEY);
			if (languageKey != null && !languageKey.equals(languageMenuItem.getData("language.key"))) {
				languageMenuItem.setData("language.key", languageKey);
				Language language = LanguageManager.findSelectedLanguage();
				languageMenuItem.setData(PREF_VALUE, language.name);
			}
		}
		catch (LanguageManagerException e) {
			Log.debug(CAT, e);
		}
	}

}
