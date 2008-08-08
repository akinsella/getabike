package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.view.support.MenuListView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;

public class LanguageListView extends MenuListView {
	
	private static final String CAT = "LANGUAGE_LIST_VIEW";
	
	private Language selectedLanguage;
	
	private Vector languageList;

	public LanguageListView(AbstractMIDlet midlet, IReturnCallback displayableReturnCallback) throws LanguageManagerException {
		super(midlet, true, displayableReturnCallback);
		init();
	}

	public LanguageListView(AbstractMIDlet midlet) throws LanguageManagerException {
		super(midlet, true);
		init();
	}
	
	private void init() throws LanguageManagerException {
		setFullScreenMode(true);
		setTitle("Choix de la langue");
		initData();
		initComponents();
		initActions();
	}
	
	private void initActions() {

		setSecondaryCommand(new Command("Annuler", true, new IAction() {

			public void run(Object data) {
				returnToPreviousDisplayable();
			}
			
		}));
		
		setPrimaryCommand(new Command("Ok", true, new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				Language language = (Language)menuItem.getData();
				LanguageManager.saveSelectedLanguage(language);
				getMidlet().setLocale(new Locale(language.localeCountry, language.localeLanguage));
				returnToPreviousDisplayable();
			}
			
		}));
	}

	private void initData() throws LanguageManagerException {
		languageList = LanguageManager.findAllLanguages();
		Log.info(CAT, "languageList: " + languageList);
		selectedLanguage = LanguageManager.findSelectedLanguage(languageList);
		Log.info(CAT, "selectedLanguage: " + languageList);
	}
	
	private void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = languageList.elements();
		while(_enum.hasMoreElements()) {
			Language language = (Language)_enum.nextElement();
			MenuItem languageMenuItem = new MenuItem(language.name);
			languageMenuItem.setData(language);
			menu.addMenuItem(languageMenuItem);
			if (language.key.equals(selectedLanguage.key)) {
				menu.setCheckedMenuItem(languageMenuItem);
			}
		}

		setMenu(menu);
	}
}
