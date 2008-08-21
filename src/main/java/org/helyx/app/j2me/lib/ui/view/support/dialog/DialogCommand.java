package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class DialogCommand {

	private static final Log log = LogFactory.getLog("DIALOG_COMMAND");

	public static final DialogCommand OK = new DialogCommand("Ok", DialogResultConstants.OK);
	public static final DialogCommand CANCEL = new DialogCommand("Annuler", DialogResultConstants.CANCEL);

	private String text;
	
	private int resultCode;
	
	public DialogCommand(String text, int resultCode) {
		super();
		this.text = text;
		this.resultCode = resultCode;
	}

	public String getText() {
		return text;
	}

	public int getResultCode() {
		return resultCode;
	}
	
}
