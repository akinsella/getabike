package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public class DialogUtil {

	private static final Log log = LogFactory.getLog("DIALOG_UTIL");
	
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

	public static DialogView showYesNoDialog(AbstractMIDlet midlet, AbstractDisplayable displayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(midlet, title, text, DialogCommand.YES, DialogCommand.NO);
		dialogView.setPreviousDisplayable(displayable);
		dialogView.setReturnCallback(dialogResultCallback);
		displayable.showDisplayable(dialogView);
		
		return dialogView;	
	}
	
	public static void showAlertMessage(AbstractMIDlet midlet, AbstractDisplayable displayable, String title, String message) {
		DialogView dialogView = new DialogView(midlet, title, message, DialogCommand.OK);
		dialogView.setPreviousDisplayable(displayable);
		dialogView.setReturnCallback(new AbstractDialogResultCallback() {
			public void onResult(DialogView dialogView) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.OK:
						dialogView.returnToPreviousDisplayable();
						break;
				}
			}
		});
		displayable.showDisplayable(dialogView);
	}

}
