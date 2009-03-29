package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.support.AbstractTextBox;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;

public class StationSearchView extends AbstractTextBox {
	
	private static final Logger logger = LoggerFactory.getLogger("STATION_SEARCH_VIEW");
	
	public static final String PREF_STATION_NAME_FILTER = "search.stationName";

	private Command cmdBack;
	private Command cmdValidate;
	
	public StationSearchView(AbstractMIDlet midlet) {
		super(midlet, "Recherche de station");
		init();
	}

	private void init() {
		loadPrefs();

		initCommands();
	}

	private void initCommands() {
		cmdBack = new Command("Annuler", Command.BACK, 1);
		textBox.addCommand(cmdBack);
		cmdValidate = new Command("Valider", Command.ITEM, 2);
		textBox.addCommand(cmdValidate);
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == cmdValidate) {
			savePrefs();
			fireReturnCallback();
		}
		if (command == cmdBack) {
			fireReturnCallback();
		}
	}
	
	private void loadPrefs() {
		String stationName = PrefManager.readPrefString(PREF_STATION_NAME_FILTER);
		if (stationName != null) {
			textBox.setString(stationName);
		}
	}
	
	private void savePrefs() {
		PrefManager.writePref(PREF_STATION_NAME_FILTER, textBox.getString());
	}
	
}
