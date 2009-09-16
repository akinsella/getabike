package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.PrefBaseListView;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class PrefListView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("PREF_LIST_VIEW");
	
	private MenuItem countryMenuItem;
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;
	private MenuItem optionMenuItem;
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
	
	private void selectCountry() {
		selectCountry(null);
	}
	
	private void selectCountry(IReturnCallback returnCallback) {
		if (returnCallback != null) {
			CityManager.showCountryListView(PrefListView.this, returnCallback);
		}
		else {
			CityManager.showCountryListView(PrefListView.this);			
		}
	}
	
	private void selectCity(String currentCountry) {
		CityManager.showCityListView(PrefListView.this, currentCountry);			
	}

	protected void initComponents() {
		
		Menu menu = new Menu();
		
		countryMenuItem = new MenuItem("Pays", new IAction() {
			public void run(Object data) {
				selectCountry();
			}
		});
		
		cityMenuItem = new MenuItem("Villes", new IAction() {
			public void run(Object data) {
				String currentCountry = CityManager.getCurrentCountry();
				if (currentCountry != null) {
					selectCity(currentCountry);		
				}
				else {
					selectCountry(new IReturnCallback() {
						
						public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
							String currentCountry = CityManager.getCurrentCountry();
							if (currentCountry != null) {
								selectCountry();
							}
						}
					});
				}
			}
		});
		
		languageMenuItem = new MenuItem("Langues", new IAction() {
			public void run(Object data) {
				LanguageManager.showLanguageView(PrefListView.this);
			}
		});
		
		optionMenuItem = new MenuItem("Options avancées", new IAction() {
			public void run(Object data) {

				PrefBaseListView optionMenuListView = new PrefBaseListView(getMidlet(), "Options avancées") {
					
					private MenuItem mapModeMenuItem;
					private MenuItem httpModeMenuItem;
					private MenuItem debugModeMenuItem;

					protected void onInit() {
						
						final AbstractDisplayable displayable = this;
						
						mapModeMenuItem = new MenuItem("Google Maps", new IAction() {
							public void run(Object data) {
								UtilManager.changeMapMode(displayable);
							}
						});
						
						httpModeMenuItem = new MenuItem("Http optimisé", new IAction() {
							public void run(Object data) {
								UtilManager.changeHttpMode(displayable);
							}
						});
						
						debugModeMenuItem = new MenuItem("Debug mode", new IAction() {
							public void run(Object data) {
								UtilManager.changeDebugMode(displayable);
							}
						});
						
						Menu optionMenu = new Menu();
						
						optionMenu.addMenuItem(mapModeMenuItem);
						optionMenu.addMenuItem(httpModeMenuItem);
						optionMenu.addMenuItem(debugModeMenuItem);
						
						setMenu(optionMenu);

					}
					
					public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
						logger.info("Current: " + current);
						logger.info("Next: " + next);
						if (next == this) {
							fetchDebugMode();
							fetchHttpMode();
							fetchMapMode();
						}
						super.beforeDisplayableSelection(current, next);
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
					
				};

				optionMenuListView.setPreviousDisplayable(PrefListView.this);
				showDisplayable(optionMenuListView);
			}
		});		
		optionMenuItem.setParentMenu(true);
		
		resetMenuItem = new MenuItem("Reset", new IAction() {
			public void run(Object data) {
				UtilManager.reset(PrefListView.this);
			}
		});
		
		menu.addMenuItem(countryMenuItem);
		menu.addMenuItem(cityMenuItem);
		menu.addMenuItem(languageMenuItem);
		menu.addMenuItem(optionMenuItem);
		menu.addMenuItem(resetMenuItem);
		
		setMenu(menu);

	}

	protected void initData() {

	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		logger.info("Current: " + current);
		logger.info("Next: " + next);
		if (next == this) {
			fetchCountryPref();
			fetchCityPref();
			fetchLanguagePref();
		}
		super.beforeDisplayableSelection(current, next);
	}

	private void fetchCountryPref() {
		String country = CityManager.getCurrentCountry();
		if (country != null) {
			countryMenuItem.setData(PREF_VALUE, getMessage("velocite.country." + country));
		}
		else {
			countryMenuItem.removeData(PREF_VALUE);
		}
	}

	private void fetchCityPref() {
		City city = CityManager.getCurrentCity();
		if (city != null) {
			cityMenuItem.setData(PREF_VALUE, city.name);
		}
		else {
			cityMenuItem.removeData(PREF_VALUE);
		}
	}

	private void fetchLanguagePref() {
		try {
			String languageKey = PrefManager.readPrefString(PrefConstants.LANGUAGE_CURRENT_KEY);
			if (languageKey != null) {
				Language language = LanguageManager.getCurrentLanguage();
				languageMenuItem.setData(PREF_VALUE, language.name);
			}
			else {
				languageMenuItem.removeData(PREF_VALUE);
			}
		}
		catch (LanguageManagerException e) {
			logger.debug(e);
		}
	}

}
