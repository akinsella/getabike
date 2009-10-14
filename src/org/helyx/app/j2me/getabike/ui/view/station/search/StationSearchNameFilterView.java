package org.helyx.app.j2me.getabike.ui.view.station.search;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.ui.displayable.support.AbstractTextBox;
import org.helyx.logging4me.Logger;


public class StationSearchNameFilterView extends AbstractTextBox {
	
	private static final Logger logger = Logger.getLogger("STATION_SEARCH_VIEW");
	
	private Command cmdBack;
	private Command cmdValidate;
	
	public StationSearchNameFilterView(AbstractMIDlet midlet) {
		super(midlet, "");
		setTitle(getMidlet().getI18NTextRenderer().renderText("view.station.search.filter.name.title"));
		init();
	}

	private void init() {
		loadPrefs();

		initCommands();
	}

	private void initCommands() {
		cmdBack = new Command(getMidlet().getI18NTextRenderer().renderText("dialog.title.cancel"), Command.BACK, 1);
		textBox.addCommand(cmdBack);
		cmdValidate = new Command(getMidlet().getI18NTextRenderer().renderText("dialog.title.validate"), Command.ITEM, 2);
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
		String stationName = PrefManager.readPrefString(PrefConstants.PREF_STATION_NAME_FILTER);
		if (stationName != null) {
			textBox.setString(stationName);
		}
	}
	
	private void savePrefs() {
		PrefManager.writePref(PrefConstants.PREF_STATION_NAME_FILTER, textBox.getString());
	}
	
}
