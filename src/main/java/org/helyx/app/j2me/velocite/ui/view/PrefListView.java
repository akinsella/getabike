package org.helyx.app.j2me.velocite.ui.view;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.PrefBaseListView;
import org.helyx.app.j2me.lib.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogResultConstants;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogView;
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

	private static final Log log = LogFactory.getLog("PREF_LIST_VIEW");
	
	private MenuItem cityMenuItem;
	private MenuItem languageMenuItem;
	private MenuItem debugMenuItem;
	private MenuItem resetMenuItem;

	public PrefListView(AbstractMIDlet midlet) {
		super(midlet, "Préférences");
		init();
	}
	
	public PrefListView(AbstractMIDlet midlet, IReturnCallback returnCallback) {
		super(midlet, "Préférences", returnCallback);
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
				CityListView cityListView;
				try {
					cityListView = new CityListView(getMidlet());
					cityListView.setPreviousDisplayable(PrefListView.this);
					showDisplayable(cityListView);
				}
				catch (CityManagerException e) {
					log.warn(e);
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
				catch (RuntimeException e) {
					log.warn(e);
					showAlertMessage("Problème de configuration", "Le fichier des langues n'est pas valide: " + e.getMessage());
				}
			}
		});
		
		debugMenuItem = new MenuItem("Debug mode", new IAction() {
			public void run(Object data) {
				DialogUtil.showYesNoDialog(getMidlet(), PrefListView.this, "Question", "Activer le mode Debug ?", new AbstractDialogResultCallback() {

					public void onResult(DialogView dialogView) {
						int resultValue = dialogView.getResultCode();
						switch (resultValue) {
							case DialogResultConstants.YES:
								Log.setThresholdLevel(Log.DEBUG);
								dialogView.returnToPreviousDisplayable();
								break;
							case DialogResultConstants.NO:
								Log.setThresholdLevel(Log.INFO);
								dialogView.returnToPreviousDisplayable();
								break;
						}
					}
				});
			}
		});
		
		
		resetMenuItem = new MenuItem("Reset", new IAction() {
			public void run(Object data) {
				PrefManager.writePrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, true);
				DialogUtil.showConfirmDialog(getMidlet(), PrefListView.this, "Confirmation", "Etes-vous sur de vouloir reseter l'application ?", new AbstractDialogResultCallback() {

					public void onResult(DialogView dialogView) {
						int resultValue = dialogView.getResultCode();
						switch (resultValue) {
							case DialogResultConstants.OK:
								DialogUtil.showMessageDialog(getMidlet(), PrefListView.this, "Attention", "L'application va quitter. L'application doit être relancée.", new AbstractDialogResultCallback() {

									public void onResult(DialogView dialogView) {
										getMidlet().exit();								
									}
								});

								break;
							case DialogResultConstants.CANCEL:
								dialogView.returnToPreviousDisplayable();
								break;
						}
					}
					
				});
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
		}
		catch (LanguageManagerException e) {
			log.debug(e);
		}
	}

	private void fetchDebugMode() {
		debugMenuItem.setData(PREF_VALUE, Log.getThresholdLevel() == Log.DEBUG ? "Oui" : "Non");
	}

}
