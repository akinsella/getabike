package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.manager.CityManagerException;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.view.support.PrefBaseListView;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class PrefListView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("PREF_LIST_VIEW");
	
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;
	private MenuItem mapModeMenuItem;
	private MenuItem httpModeMenuItem;
	private MenuItem debugModeMenuItem;
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
		
		mapModeMenuItem = new MenuItem("Google Map", new IAction() {
			public void run(Object data) {
				UtilManager.changeMapMode(PrefListView.this);
			}
		});
		
		httpModeMenuItem = new MenuItem("Http optimisé", new IAction() {
			public void run(Object data) {
				UtilManager.changeHttpMode(PrefListView.this);
			}
		});
		
		debugModeMenuItem = new MenuItem("Debug mode", new IAction() {
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
		menu.addMenuItem(mapModeMenuItem);
		menu.addMenuItem(httpModeMenuItem);
		menu.addMenuItem(debugModeMenuItem);
		menu.addMenuItem(resetMenuItem);
		
		setMenu(menu);
	}

	protected void initData() {

	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		logger.info("Current: " + current);
		logger.info("Next: " + next);
		if (next == this) {
			fetchCityPref();
			fetchLanguagePref();
			fetchDebugMode();
			fetchHttpMode();
			fetchMapMode();
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
			logger.debug(e);
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
			logger.debug(e);
		}
	}

	private void fetchDebugMode() {
		boolean isDebugModeActive = UtilManager.isDebugModeActive();
		if (logger.isDebugEnabled()) {
			logger.debug("Debug mode active: " + isDebugModeActive);	
		}
		debugModeMenuItem.setData(PREF_VALUE, isDebugModeActive ? "Oui" : "Non");
	}

	private void fetchHttpMode() {
		boolean isOptimizedHttpModeActive = PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED);
		if (logger.isDebugEnabled()) {
			logger.debug("Optimized Http mode active: " + isOptimizedHttpModeActive);	
		}
		httpModeMenuItem.setData(PREF_VALUE, isOptimizedHttpModeActive ? "Oui" : "Non");
	}

	private void fetchMapMode() {
		boolean isMapModeActive = PrefManager.readPrefBoolean(UtilManager.MAP_MODE_ENABLED);
		if (logger.isDebugEnabled()) {
			logger.debug("Map mode active: " + isMapModeActive);	
		}
		mapModeMenuItem.setData(PREF_VALUE, isMapModeActive ? "Activé" : "Désactivé");
	}

}
