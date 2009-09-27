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
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
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
		super(midlet, "");
		setTitle(getMessage("view.pref.title"));
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
		
		countryMenuItem = new MenuItem(getMessage("view.pref.item.country"), new IAction() {
			public void run(Object data) {
				selectCountry();
			}
		});
		
		cityMenuItem = new MenuItem(getMessage("view.pref.item.cities"), new IAction() {
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
		
		languageMenuItem = new MenuItem(getMessage("view.pref.item.languages"), new IAction() {
			public void run(Object data) {
				LanguageManager.showLanguageView(PrefListView.this);
			}
		});
		
		optionMenuItem = new MenuItem(getMessage("view.pref.item.advances.options"), new IAction() {
			public void run(Object data) {

				PrefBaseListView optionMenuListView = new PrefBaseListView(getMidlet(), getMessage("view.pref.item.advances.options")) {
					
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
						
						mapModeMenuItem = new MenuItem(getMessage("view.pref.item.google.maps"), new IAction() {
							public void run(Object data) {
								UtilManager.changeMapMode(currentDisplayable);
							}
						});
						
						httpModeMenuItem = new MenuItem(getMessage("view.pref.item.http.optimized"), new IAction() {
							public void run(Object data) {
								UtilManager.changeHttpMode(currentDisplayable);
							}
						});
						
						debugModeMenuItem = new MenuItem(getMessage("view.pref.item.debug.mode"), new IAction() {
							public void run(Object data) {
								UtilManager.changeDebugMode(currentDisplayable);
							}
						});
						
						platformMenuItem = new MenuItem(getMessage("view.pref.item.platform"), new IAction() {
							
							public void run(Object data) {
								DialogUtil.showAlertMessage(currentDisplayable, getMessage("view.pref.item.platform"), (String)platformMenuItem.getData(PrefListView.PREF_VALUE_DETAILS));
							}
						});
						
						platformMenuItem.setParentMenu(true);
						profilesMenuItem = new MenuItem(getMessage("view.pref.item.profiles"));
						configurationMenuItem = new MenuItem(getMessage("view.pref.item.configuration"));
						fcVersionMenuItem = new MenuItem(getMessage("view.pref.item.file.support"));
						encodingMenuItem = new MenuItem(getMessage("view.pref.item.encoding"));
						screenSizeItem = new MenuItem(getMessage("view.pref.item.screen"));
						
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
						debugModeMenuItem.setData(PREF_VALUE, isDebugModeActive ? getMessage("view.pref.value.yes") : getMessage("view.pref.value.no"));
					}
					
					private void fetchSystemInformations() {
						String encoding = System.getProperty("microedition.encoding");
						encodingMenuItem.setData(PREF_VALUE, encoding != null ? encoding : getMessage("view.pref.value.unknown"));
						String configuration = System.getProperty("microedition.configuration");
						configurationMenuItem.setData(PREF_VALUE, configuration != null ? configuration : getMessage("view.pref.value.unknown"));
						String platform = System.getProperty("microedition.platform");
						platformMenuItem.setData(PREF_VALUE_DETAILS, platform != null ? platform : getMessage("view.pref.value.unknown"));
						String profiles = System.getProperty("microedition.profiles");
						profilesMenuItem.setData(PREF_VALUE, profiles != null ? profiles : getMessage("view.pref.value.unknown"));
						String fcVersion = System.getProperty("microedition.io.file.FileConnection.version");
						fcVersionMenuItem.setData(PREF_VALUE, fcVersion != null ? fcVersion : getMessage("view.pref.value.no"));
						screenSizeItem.setData(PREF_VALUE, viewCanvas.getWidth() + "x" + viewCanvas.getHeight());
					}

					private void fetchHttpMode() {
						boolean isOptimizedHttpModeActive = PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED);
						if (logger.isDebugEnabled()) {
							logger.debug("Optimized Http mode active: " + isOptimizedHttpModeActive);	
						}
						httpModeMenuItem.setData(PREF_VALUE, isOptimizedHttpModeActive ? getMessage("view.pref.value.yes") : getMessage("view.pref.value.no"));
					}

					private void fetchMapMode() {
						boolean isMapModeActive = PrefManager.readPrefBoolean(UtilManager.MAP_MODE_ENABLED);
						if (logger.isDebugEnabled()) {
							logger.debug("Map mode active: " + isMapModeActive);	
						}
						mapModeMenuItem.setData(PREF_VALUE, isMapModeActive ? getMessage("view.pref.value.activated") : getMessage("view.pref.value.deactivated"));
					}
					
				};

				optionMenuListView.setPreviousDisplayable(PrefListView.this);
				showDisplayable(optionMenuListView);
			}
		});		
		optionMenuItem.setParentMenu(true);
		
		versionMenuItem = new MenuItem(getMessage("view.pref.item.version"));
		
		resetMenuItem = new MenuItem(getMessage("view.pref.item.reset"), new IAction() {
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
			PrefManager.writePrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, true);
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
			PrefManager.writePrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, true);
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
			DialogUtil.showAlertMessage(this, getMessage("dialog.title.error"), getMessage("view.pref.check.exception.message"), new OkResultCallback() {
				
				public void onOk(DialogView dialogView, Object data) {
					getMidlet().exit();
				}
			});

		}
	}

}
