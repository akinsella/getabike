package org.helyx.app.j2me.velocite.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManager;
import org.helyx.app.j2me.velocite.data.language.manager.LanguageManagerException;
import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.i18n.Locale;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.view.support.MenuListView;
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.helyx4me.ui.widget.menu.Menu;
import org.helyx.helyx4me.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class LanguageListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("LANGUAGE_LIST_VIEW");
	
	private Language selectedLanguage;
	
	private Vector languageList;

	public LanguageListView(AbstractMIDlet midlet) {
		super(midlet, "Choix de la langue", true);
		init();
	}
	
	private void init() {
		initData();
		initComponents();
		initActions();
	}
	
	protected void initActions() {

		setSecondaryCommand(new Command("Annuler", true, new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setPrimaryCommand(new Command("Ok", true, new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				Language language = (Language)menuItem.getData();
				LanguageManager.saveSelectedLanguage(language);
				getMidlet().setLocale(new Locale(language.localeCountry, language.localeLanguage));
				fireReturnCallback();
			}
			
		}));
	}

	protected void initData() {
		languageList = LanguageManager.findAllLanguages();
		logger.info("languageList: " + languageList);
		try {
			selectedLanguage = LanguageManager.findSelectedLanguage(languageList);
		}
		catch (LanguageManagerException lme) {
			throw new RuntimeException(lme.getMessage());
		}
		logger.info("selectedLanguage: " + languageList);
	}
	
	protected void initComponents() {
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
