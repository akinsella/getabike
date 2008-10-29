package org.helyx.app.j2me.velocite.util;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogResultConstants;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.velocite.PrefConstants;

public class UtilManager {

	public static void changeDebugMode(AbstractDisplayable abstractDisplayable, AbstractDisplayable previousDisplayable) {
		DialogUtil.showYesNoDialog(abstractDisplayable.getMidlet(), previousDisplayable, "Question", "Activer le mode Debug ?", new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.YES:
						Log.setThresholdLevel(Log.DEBUG);
						dialogView.fireReturnCallback();
						break;
					case DialogResultConstants.NO:
						Log.setThresholdLevel(Log.INFO);
						dialogView.fireReturnCallback();
						break;
				}
			}
		});

	}

	public static void reset(final AbstractDisplayable abstractDisplayable, final AbstractDisplayable previousDisplayable) {
		PrefManager.writePrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, true);
		DialogUtil.showConfirmDialog(abstractDisplayable.getMidlet(), previousDisplayable, "Confirmation", "Etes-vous sur de vouloir reseter l'application ?", new AbstractDialogResultCallback() {

			public void onResult(DialogView dialogView) {
				int resultValue = dialogView.getResultCode();
				switch (resultValue) {
					case DialogResultConstants.OK:
						DialogUtil.showMessageDialog(abstractDisplayable.getMidlet(), previousDisplayable, "Attention", "L'application va quitter. L'application doit être relancée.", new AbstractDialogResultCallback() {

							public void onResult(DialogView dialogView) {
								abstractDisplayable.getMidlet().exit();								
							}
						});

						break;
					case DialogResultConstants.CANCEL:
						dialogView.fireReturnCallback();
						break;
				}
			}
			
		});
	}
	
}
