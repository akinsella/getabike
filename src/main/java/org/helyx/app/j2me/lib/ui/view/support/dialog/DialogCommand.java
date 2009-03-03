package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class DialogCommand {

	private static final Logger logger = LoggerFactory.getLogger("DIALOG_COMMAND");

	public static final DialogCommand YES = new DialogCommand("Oui", DialogResultConstants.YES);
	public static final DialogCommand NO = new DialogCommand("Non", DialogResultConstants.NO);

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
