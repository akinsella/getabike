package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
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
import org.helyx.app.j2me.velocite.util.UtilManager;

public class PrefListView extends PrefBaseListView {

	private static final Log log = LogFactory.getLog("PREF_LIST_VIEW");
	
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;
	private MenuItem debugMenuItem;
	private MenuItem resetMenuItem;

	public PrefListView(AbstractMIDlet midlet) {
		super(midlet, "Préférences");
		init();
	}
	 
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	protected void initComponents() {
		
		Menu menu = new Menu();
		
		cityMenuItem = new MenuItem("Villes", new IAction() {
			public void run(Object data) {
				CityManager.showCityListView(PrefListView.this);
			}
		});
		
		languageMenuItem = new MenuItem("Langues", new IAction() {
			public void run(Object data) {
				LanguageManager.showLanguageView(PrefListView.this);
			}
		});
		
		debugMenuItem = new MenuItem("Debug mode", new IAction() {
			public void run(Object data) {
				UtilManager.changeDebugMode(PrefListView.this);
			}
		});
		
		
		resetMenuItem = new MenuItem("Reset", new IAction() {
			public void run(Object data) {
				UtilManager.reset(PrefListView.this);
			}
		});
		
		
		menu.addMenuItem(cityMenuItem);
		menu.addMenuItem(languageMenuItem);
		menu.addMenuItem(debugMenuItem);
		menu.addMenuItem(resetMenuItem);
		
		setMenu(menu);
	}

	protected void initData() {

	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		log.info("Current: " + current);
		log.info("Next: " + next);
		if (next == this) {
			fetchCityPref();
			fetchLanguagePref();
			fetchDebugMode();
		}
		super.beforeDisplayableSelection(current, next);
	}

	private void fetchCityPref() {
		try {
			String cityKey = PrefManager.readPrefString(PrefConstants.CITY_SELECTED_KEY);
			if (cityKey != null && !cityKey.equals(cityMenuItem.getData("city.key"))) {
				cityMenuItem.setData("city.key", cityKey);
				City city = CityManager.findSelectedCity();
				if (city != null) {
					cityMenuItem.setData(PREF_VALUE, city.name);
				}
			}
			else {
				cityMenuItem.removeData("city.key");
			}
		}
		catch (CityManagerException e) {
			log.debug(e);
		}
	}

	private void fetchLanguagePref() {
		try {
			String languageKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_SELECTED_KEY);
			if (languageKey != null && !languageKey.equals(languageMenuItem.getData("language.key"))) {
				languageMenuItem.setData("language.key", languageKey);
				Language language = LanguageManager.findSelectedLanguage();
				languageMenuItem.setData(PREF_VALUE, language.name);
			}
			else {
				cityMenuItem.removeData("language.key");
			}
		}
		catch (LanguageManagerException e) {
			log.debug(e);
		}
	}

	private void fetchDebugMode() {
		debugMenuItem.setData(PREF_VALUE, Log.getThresholdLevel() == Log.DEBUG ? "Oui" : "Non");
	}

}
