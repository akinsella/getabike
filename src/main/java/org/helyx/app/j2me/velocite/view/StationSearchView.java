package org.helyx.app.j2me.velocite.view;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.displayable.AbstractTextBox;
import org.helyx.app.j2me.lib.ui.displayable.IDisplayableReturnCallback;

public class StationSearchView extends AbstractTextBox {
	
	private static final String CAT = "STATION_SEARCH_VIEW";
	
	public static final String PREF_STATION_NAME = "search.stationName";
	public static final String PREF_MAX_STATION_NUMBER = "search.maxStationNumber";

	private Command cmdBack;
	private Command cmdValidate;
	
	private IDisplayableReturnCallback displayableReturnCallback;
	
	public StationSearchView(MIDlet midlet, IDisplayableReturnCallback displayableReturnCallback) {
		super(midlet, "Recherche de station");
		this.displayableReturnCallback = displayableReturnCallback;
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
		
		textBox.setCommandListener(this);
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == cmdValidate) {
			savePrefs();
			returnToPreviousDisplayable();
			displayableReturnCallback.onReturn(null);
		}
		if (command == cmdBack) {
			returnToPreviousDisplayable();
			displayableReturnCallback.onReturn(null);
		}
	}
	
	private void loadPrefs() {
		String stationName = PrefManager.readPrefValue(PREF_STATION_NAME);
		if (stationName != null) {
			textBox.setString(stationName);
		}
	}
	
	private void savePrefs() {
		PrefManager.writePref(PREF_STATION_NAME, textBox.getString());
	}
	
}
