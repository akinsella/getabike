package org.helyx.app.j2me.getabike.ui.view;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.data.app.manager.AppManager;
import org.helyx.app.j2me.getabike.data.city.domain.City;
import org.helyx.app.j2me.getabike.data.city.manager.CityManager;
import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.data.language.manager.LanguageManager;
import org.helyx.app.j2me.getabike.util.ApiUtil;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.BasicReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.pref.PrefBaseListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;

public class PrefListView extends PrefBaseListView {

	private static final Logger logger = Logger.getLogger("PREF_LIST_VIEW");
	
	private MenuItem countryMenuItem;
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;
	private MenuItem optionMenuItem;
	private MenuItem resetMenuItem;
	private MenuItem versionMenuItem;
	private MenuItem updateAppMenuItem;
	private MenuItem updateCartoMetaDataMenuItem;
	
	private Throwable prefException;

	public PrefListView(AbstractMIDlet midlet) {
		super(midlet, "view.pref.title");
		init();
	}
	 
	private void init() {
		initActions();
		initData();
		initComponents();
	}

	protected void initComponents() {
		
		Menu menu = new Menu();
		
		countryMenuItem = new MenuItem("view.pref.item.country", new IAction() {
			public void run(Object data) {
				CityManager.selectCountry(PrefListView.this, new BasicReturnCallback(PrefListView.this));
			}
		});
		
		cityMenuItem = new MenuItem("view.pref.item.cities", new IAction() {
			public void run(Object data) {
				CityManager.selectCity(PrefListView.this, new BasicReturnCallback(PrefListView.this), true);
			}
		});
		
		languageMenuItem = new MenuItem("view.pref.item.languages", new IAction() {
			public void run(Object data) {
				LanguageManager.showLanguageView(PrefListView.this);
			}
		});
		
		optionMenuItem = new MenuItem("view.pref.item.advanced.options", new IAction() {
			public void run(Object data) {

				PrefBaseListView optionMenuListView = new PrefBaseListView(getMidlet(), "view.pref.item.advanced.options") {
					
					private MenuItem mapModeMenuItem;
					private MenuItem geoLocModeMenuItem;
					private MenuItem httpModeMenuItem;
					private MenuItem debugModeMenuItem;
					private MenuItem platformMenuItem;
					private MenuItem profilesMenuItem;
					private MenuItem configurationMenuItem;
					private MenuItem appUuidMenuItem;
					private MenuItem fileApiVersionMenuItem;
					private MenuItem locationApiVersionMenuItem;
					private MenuItem pimApiVersionMenuItem;
					private MenuItem encodingMenuItem;
					private MenuItem screenSizeItem;

					protected void onInit() {
						
						final AbstractDisplayable currentDisplayable = this;
						
						mapModeMenuItem = new MenuItem("view.pref.item.google.maps", new IAction() {
							public void run(Object data) {
								UtilManager.changeMapMode(currentDisplayable);
							}
						});
						
						geoLocModeMenuItem = new MenuItem("view.pref.item.operator.geoloc", new IAction() {
							public void run(Object data) {
								UtilManager.changeGeoLocMode(currentDisplayable);
							}
						});
						
						httpModeMenuItem = new MenuItem("view.pref.item.http.optimized", new IAction() {
							public void run(Object data) {
								UtilManager.changeHttpMode(currentDisplayable);
							}
						});
						
						debugModeMenuItem = new MenuItem("view.pref.item.debug.mode", new IAction() {
							public void run(Object data) {
								UtilManager.changeDebugMode(currentDisplayable);
							}
						});

						appUuidMenuItem = new MenuItem("view.pref.item.app.uuid", new IAction() {
							public void run(Object data) {
								DialogUtil.showAlertMessage(currentDisplayable, "view.pref.item.app.uuid", (String)appUuidMenuItem.getData(PrefListView.PREF_VALUE_DETAILS));
							}
						});
						appUuidMenuItem.setParentMenu(true);
						
						platformMenuItem = new MenuItem("view.pref.item.platform", new IAction() {
							
							public void run(Object data) {
								DialogUtil.showAlertMessage(currentDisplayable, "view.pref.item.platform", (String)platformMenuItem.getData(PrefListView.PREF_VALUE_DETAILS));
							}
						});
						
						platformMenuItem.setParentMenu(true);
						profilesMenuItem = new MenuItem("view.pref.item.profiles");
						configurationMenuItem = new MenuItem("view.pref.item.configuration");
					
						fileApiVersionMenuItem = new MenuItem("view.pref.item.api.file.support");
						locationApiVersionMenuItem = new MenuItem("view.pref.item.api.location.support");
						pimApiVersionMenuItem = new MenuItem("view.pref.item.api.pim.support");
						encodingMenuItem = new MenuItem("view.pref.item.encoding");
						screenSizeItem = new MenuItem("view.pref.item.screen");
						
						Menu optionMenu = new Menu();
						
						optionMenu.addMenuItem(mapModeMenuItem);
						optionMenu.addMenuItem(geoLocModeMenuItem);
						optionMenu.addMenuItem(httpModeMenuItem);
						optionMenu.addMenuItem(debugModeMenuItem);
						optionMenu.addMenuItem(appUuidMenuItem);
						optionMenu.addMenuItem(platformMenuItem);
						optionMenu.addMenuItem(profilesMenuItem);
						optionMenu.addMenuItem(configurationMenuItem);
						optionMenu.addMenuItem(fileApiVersionMenuItem);
						optionMenu.addMenuItem(pimApiVersionMenuItem);
						optionMenu.addMenuItem(locationApiVersionMenuItem);
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
							fetchGeoLocMode();
							fetchSystemInformations();
							fetchAppUuid();
						}
						super.beforeDisplayableSelection(current, next);
					}

					private void fetchAppUuid() {
						String appUuid = PrefManager.readPrefString(PrefConstants.APP_UUID);
						if (logger.isDebugEnabled()) {
							logger.debug("App UUID: " + appUuid);	
						}
						appUuidMenuItem.setData(PREF_VALUE_DETAILS, appUuid);
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
						String fileApiVersion = ApiUtil.getFileApiVersion();
						fileApiVersionMenuItem.setData(PREF_VALUE, fileApiVersion != null ? fileApiVersion : getMessage("view.pref.value.no"));
						String locationApiVersion = ApiUtil.getLocationApiVersion();
						locationApiVersionMenuItem.setData(PREF_VALUE, locationApiVersion != null ? locationApiVersion : getMessage("view.pref.value.no"));
						String pimApiVersion = ApiUtil.getPimApiVersion();
						pimApiVersionMenuItem.setData(PREF_VALUE, pimApiVersion != null ? pimApiVersion : getMessage("view.pref.value.no"));
						screenSizeItem.setData(PREF_VALUE, viewCanvas.getWidth() + "x" + viewCanvas.getHeight());
					}

					private void fetchHttpMode() {
						boolean isOptimizedHttpModeActive = PrefManager.readPrefBoolean(PrefConstants.OPTIMIZED_HTTP_MODE_ENABLED);
						if (logger.isDebugEnabled()) {
							logger.debug("Optimized Http mode active: " + isOptimizedHttpModeActive);	
						}
						httpModeMenuItem.setData(PREF_VALUE, isOptimizedHttpModeActive ? getMessage("view.pref.value.yes") : getMessage("view.pref.value.no"));
					}

					private void fetchMapMode() {
						boolean isMapModeActive = PrefManager.readPrefBoolean(PrefConstants.MAP_MODE_ENABLED);
						if (logger.isDebugEnabled()) {
							logger.debug("Map mode active: " + isMapModeActive);	
						}
						mapModeMenuItem.setData(PREF_VALUE, isMapModeActive ? getMessage("view.pref.value.activated") : getMessage("view.pref.value.deactivated"));
					}
					
					private void fetchGeoLocMode() {
						boolean isGeoLocModeActive = PrefManager.readPrefBoolean(PrefConstants.COST_ALLOWED_GEO_LOCALIZATION);
						if (logger.isDebugEnabled()) {
							logger.debug("Geo loc operator mode active: " + isGeoLocModeActive);	
						}
						geoLocModeMenuItem.setData(PREF_VALUE, isGeoLocModeActive ? getMessage("view.pref.value.activated") : getMessage("view.pref.value.deactivated"));
					}
					
				};

				optionMenuListView.setPreviousDisplayable(PrefListView.this);
				showDisplayable(optionMenuListView);
			}
		});		
		
		optionMenuItem.setParentMenu(true);
		
		versionMenuItem = new MenuItem("view.pref.item.version");
		
		updateAppMenuItem = new MenuItem("view.pref.item.check.update", new IAction() {
			public void run(Object data) {
				AppManager.checkUpdateApplication(PrefListView.this, false, false);
			}
		});
		
		updateCartoMetaDataMenuItem = new MenuItem("view.pref.item.check.update.metadata", new IAction() {
			public void run(Object data) {
				AppManager.updateCartoMetaData(PrefListView.this);
			}
		});
		
		resetMenuItem = new MenuItem("view.pref.item.reset", new IAction() {
			public void run(Object data) {
				UtilManager.reset(PrefListView.this);
			}
		});
		
		menu.addMenuItem(countryMenuItem);
		menu.addMenuItem(cityMenuItem);
		menu.addMenuItem(languageMenuItem);
		menu.addMenuItem(optionMenuItem);
		menu.addMenuItem(versionMenuItem);
		menu.addMenuItem(updateAppMenuItem);
		menu.addMenuItem(updateCartoMetaDataMenuItem);
		menu.addMenuItem(resetMenuItem);
		
		setMenu(menu);

	}

	protected void initData() {

	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		if (logger.isDebugEnabled()) {
			logger.debug("Current: " + current);
			logger.debug("Next: " + next);
		}
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
				countryMenuItem.setData(PREF_VALUE, getMessage("getabike.country." + country));
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
				languageMenuItem.setData(PREF_VALUE, getMessage("getabike.language." + language.key));
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
			String version = PrefManager.readPrefString(PrefConstants.APP_VERSION);
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
			DialogUtil.showAlertMessage(this, "dialog.title.error", getMessage("view.pref.check.exception.message"), new OkResultCallback() {
				
				public void onOk(DialogView dialogView, Object data) {
					getMidlet().exit();
				}
			});

		}
	}

}
