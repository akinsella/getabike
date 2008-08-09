package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public class DialogUtil {

	private static final String CAT = "DIALOG_UTIL";
	
	public DialogUtil() {
		super();
	}

	public static DialogView showMessageDialog(AbstractMIDlet midlet, AbstractDisplayable displayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(midlet, title, text, DialogCommand.OK);
		dialogView.setPreviousDisplayable(displayable);
		dialogView.setReturnCallback(dialogResultCallback);
		displayable.showDisplayable(dialogView);
		return dialogView;	
	}

	public static DialogView showConfirmDialog(AbstractMIDlet midlet, AbstractDisplayable displayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(midlet, title, text, DialogCommand.OK, DialogCommand.CANCEL);
		dialogView.setPreviousDisplayable(displayable);
		dialogView.setReturnCallback(dialogResultCallback);
		displayable.showDisplayable(dialogView);
		
		return dialogView;	
	}
	
}
