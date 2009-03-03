package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;

public class DialogUtil {

	private static final Logger logger = LoggerFactory.getLogger("DIALOG_UTIL");
	
	public DialogUtil() {
		super();
	}

	public static DialogView showMessageDialog(AbstractDisplayable currentDisplayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(currentDisplayable.getMidlet(), title, text, DialogCommand.OK);
		dialogView.setReturnCallback(dialogResultCallback);
		currentDisplayable.showDisplayable(dialogView);
		return dialogView;	
	}

	public static DialogView showConfirmDialog(AbstractDisplayable currentDisplayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(currentDisplayable.getMidlet(), title, text, DialogCommand.OK, DialogCommand.CANCEL);
		dialogView.setReturnCallback(dialogResultCallback);
		currentDisplayable.showDisplayable(dialogView);
		
		return dialogView;	
	}

	public static DialogView showYesNoDialog(AbstractDisplayable currentDisplayable, String title, String text, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(currentDisplayable.getMidlet(), title, text, DialogCommand.YES, DialogCommand.NO);
		dialogView.setReturnCallback(dialogResultCallback);
		currentDisplayable.showDisplayable(dialogView);
		
		return dialogView;	
	}
	
	public static void showAlertMessage(final AbstractDisplayable currentDisplayable, String title, String message) {
		DialogView dialogView = new DialogView(currentDisplayable.getMidlet(), title, message, DialogCommand.OK);
		dialogView.setReturnCallback(new AbstractDialogResultCallback() {
			public void onResult(DialogView dialogView, Object data) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.OK:
						dialogView.showDisplayable(currentDisplayable);
						break;
				}
			}
		});
		currentDisplayable.showDisplayable(dialogView);
	}

}
