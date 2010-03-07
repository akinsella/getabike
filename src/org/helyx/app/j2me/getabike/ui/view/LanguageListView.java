package org.helyx.app.j2me.getabike.ui.view;

import java.util.Enumeration;
import java.util.Vector;

import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.data.language.manager.LanguageManager;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.widget.ImageSet;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.Menu;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;


public class LanguageListView extends MenuListView {
	
	private static final Logger logger = Logger.getLogger("LANGUAGE_LIST_VIEW");
	
	private Language currentLanguage;
	
	private Vector languageList;

	public LanguageListView(AbstractMIDlet midlet) {
		super(midlet, "", true);
		setTitle("view.language.title");
		init();
	}
	
	private void init() {
		initData();
		initComponents();
		initActions();
	}
	
	protected void initActions() {

		setSecondaryCommand(new Command("command.cancel", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
		
		setPrimaryCommand(new Command("command.ok", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				getMenu().setCheckedMenuItem(getMenu().getSelectedMenuItem());
				MenuItem menuItem = getMenu().getCheckedMenuItem();
				
				Language language = (Language)menuItem.getData();
				LanguageManager.setCurrentLanguage(language);
				getMidlet().setLocale(new Locale(language.localeCountry, language.localeLanguage));
				getMidlet().loadCurrentLocale();
				fireReturnCallback();
			}
			
		}));
	}

	protected void initData() {
		languageList = LanguageManager.findAllLanguages();
		logger.info("languageList: " + languageList);
		
		currentLanguage = LanguageManager.getCurrentLanguage();
		
		if (currentLanguage == null) {
			throw new RuntimeException("No current language configured");
		}
		
		logger.info("selectedLanguage: " + languageList);
	}
	
	protected void initComponents() {
		Menu menu = new Menu();

		Enumeration _enum = languageList.elements();
		while(_enum.hasMoreElements()) {
			Language language = (Language)_enum.nextElement();
			MenuItem languageMenuItem = new MenuItem("getabike.language." + language.key);
			languageMenuItem.setImageSet(new ImageSet(getTheme().getString("getabike.country." + language.key + ".flag")));
			languageMenuItem.setData(language);
			menu.addMenuItem(languageMenuItem);
			if (language.key.equals(currentLanguage.key)) {
				menu.setCheckedMenuItem(languageMenuItem);
			}
		}

		setMenu(menu);
	}
}
