package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.AbstractDisplayable;
import org.helyx.helyx4me.ui.displayable.callback.IReturnCallback;
import org.helyx.helyx4me.ui.view.support.PrefBaseListView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
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
	private MenuItem versionMenuItem;
	
	private Throwable prefException;

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
					private MenuItem platformMenuItem;
					private MenuItem profilesMenuItem;
					private MenuItem configurationMenuItem;
					private MenuItem fcVersionMenuItem;
					private MenuItem encodingMenuItem;
					private MenuItem screenSizeItem;

					protected void onInit() {
						
						final AbstractDisplayable currentDisplayable = this;
						
						mapModeMenuItem = new MenuItem("Google Maps", new IAction() {
							public void run(Object data) {
								UtilManager.changeMapMode(currentDisplayable);
							}
						});
						
						httpModeMenuItem = new MenuItem("Http optimisé", new IAction() {
							public void run(Object data) {
								UtilManager.changeHttpMode(currentDisplayable);
							}
						});
						
						debugModeMenuItem = new MenuItem("Debug mode", new IAction() {
							public void run(Object data) {
								UtilManager.changeDebugMode(currentDisplayable);
							}
						});
						
						platformMenuItem = new MenuItem("Plateforme", new IAction() {
							
							public void run(Object data) {
								DialogUtil.showAlertMessage(currentDisplayable, "Plateforme", (String)platformMenuItem.getData(PrefListView.PREF_VALUE_DETAILS));
							}
						});
						
						platformMenuItem.setParentMenu(true);
						profilesMenuItem = new MenuItem("Profiles");
						configurationMenuItem = new MenuItem("Configuration");
						fcVersionMenuItem = new MenuItem("Support fichier");
						encodingMenuItem = new MenuItem("Encoding");
						screenSizeItem = new MenuItem("Ecran");
						
						Menu optionMenu = new Menu();
						
						optionMenu.addMenuItem(mapModeMenuItem);
						optionMenu.addMenuItem(httpModeMenuItem);
						optionMenu.addMenuItem(debugModeMenuItem);
						optionMenu.addMenuItem(platformMenuItem);
						optionMenu.addMenuItem(profilesMenuItem);
						optionMenu.addMenuItem(configurationMenuItem);
						optionMenu.addMenuItem(fcVersionMenuItem);
						optionMenu.addMenuItem(encodingMenuItem);
						optionMenu.addMenuItem(screenSizeItem);
						
						setMenu(optionMenu);

					}
					
					public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
						logger.info("Current: " + current);
						logger.info("Next: " + next);
						if (next == this) {
							fetchDebugMode();
							fetchHttpMode();
							fetchMapMode();
							fetchSystemInformations();
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
					
					private void fetchSystemInformations() {
						String encoding = System.getProperty("microedition.encoding");
						encodingMenuItem.setData(PREF_VALUE, encoding != null ? encoding : "Inconnu");
						String configuration = System.getProperty("microedition.configuration");
						configurationMenuItem.setData(PREF_VALUE, configuration != null ? configuration : "Inconnu");
						String platform = System.getProperty("microedition.platform");
						platformMenuItem.setData(PREF_VALUE_DETAILS, platform != null ? platform : "Inconnu");
						String profiles = System.getProperty("microedition.profiles");
						profilesMenuItem.setData(PREF_VALUE, profiles != null ? profiles : "Inconnu");
						String fcVersion = System.getProperty("microedition.io.file.FileConnection.version");
						fcVersionMenuItem.setData(PREF_VALUE, fcVersion != null ? fcVersion : "Non");
						screenSizeItem.setData(PREF_VALUE, viewCanvas.getWidth() + "x" + viewCanvas.getHeight());
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
		
		versionMenuItem = new MenuItem("Version");
		
		resetMenuItem = new MenuItem("Reset", new IAction() {
			public void run(Object data) {
				UtilManager.reset(PrefListView.this);
			}
		});
		
		menu.addMenuItem(countryMenuItem);
		menu.addMenuItem(cityMenuItem);
		menu.addMenuItem(languageMenuItem);
		menu.addMenuItem(optionMenuItem);
		menu.addMenuItem(versionMenuItem);
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
			fetchVersion();
		}
		super.beforeDisplayableSelection(current, next);
	}

	
	public void afterDisplayableSelection(AbstractDisplayable previous, AbstractDisplayable current) {
		super.afterDisplayableSelection(previous, current);
		
		if (current == this) {
			checkExceptionOnPrefLoad();
		}
	}

	private void fetchCountryPref() {
		try {
			String country = CityManager.getCurrentCountry();
			if (country != null) {
				countryMenuItem.setData(PREF_VALUE, getMessage("velocite.country." + country));
			}
			else {
				countryMenuItem.removeData(PREF_VALUE);
			}
		}
		catch(Throwable t) {
			logger.warn(t);
			PrefManager.readPrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
			prefException = t;
		}
	}

	private void fetchCityPref() {
		try {
			City city = CityManager.getCurrentCity();
			if (city != null) {
				cityMenuItem.setData(PREF_VALUE, city.name);
			}
			else {
				cityMenuItem.removeData(PREF_VALUE);
			}
		}
		catch(Throwable t) {
			logger.warn(t);
			prefException = t;
			PrefManager.readPrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
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
		catch(Throwable t) {
			logger.warn(t);
		}
	}

	private void fetchVersion() {
		try {
			String version = PrefManager.readPrefString(PrefConstants.MIDLET_VERSION);
			if (version != null) {
				versionMenuItem.setData(PREF_VALUE,version);
			}
			else {
				versionMenuItem.removeData(PREF_VALUE);
			}
		}
		catch(Throwable t) {
			logger.warn(t);
		}
	}

	private void checkExceptionOnPrefLoad() {
		if (prefException != null) {
			DialogUtil.showAlertMessage(this, "Erreur", "Une erreur critique est survenue, l'application doit être redémarrée");

		}
	}

}
