/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.view.support.dialog;

import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogCommand;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.AbstractDialogResultCallback;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.logging4me.Logger;


public class DialogUtil {

	private static final Logger logger = Logger.getLogger("DIALOG_UTIL");
	
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
	
	public static void showAlertMessage(AbstractDisplayable currentDisplayable, String title, String message) {
		showAlertMessage(currentDisplayable, title, message, null);
	}
	
	public static void showAlertMessage(final AbstractDisplayable currentDisplayable, String title, String message, AbstractDialogResultCallback dialogResultCallback) {
		DialogView dialogView = new DialogView(currentDisplayable.getMidlet(), title, message, DialogCommand.OK);

		if (dialogResultCallback != null) {
			dialogView.setReturnCallback(dialogResultCallback);
		}
		else {
			dialogView.setReturnCallback(new OkResultCallback() {
				public void onOk(DialogView dialogView, Object data) {
					dialogView.showDisplayable(currentDisplayable);
				}
			});
		}
		currentDisplayable.showDisplayable(dialogView);
	}

}
