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
package org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback;

import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.DialogResultConstants;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.AbstractDialogResultCallback;


public abstract class DialogResultCallbackAdapter extends AbstractDialogResultCallback {
	
	public DialogResultCallbackAdapter() {
		super(); 
	}
	
	public void onResult(DialogView dialogView, Object data) {
		int resultValue = dialogView.getResultCode();
		switch (resultValue) {
			case DialogResultConstants.ABORT:
				onAbort(dialogView, data);
				break;
			case DialogResultConstants.CANCEL:
				onCancel(dialogView, data);
				break;
			case DialogResultConstants.CONFIRM:
				onConfirm(dialogView, data);
				break;
			case DialogResultConstants.NO:
				onNo(dialogView, data);
				break;
			case DialogResultConstants.OK:
				onOk(dialogView, data);
				break;
			case DialogResultConstants.RETRY:
				onRetry(dialogView, data);
				break;
			case DialogResultConstants.YES:
				onYes(dialogView, data);
				break;
			case DialogResultConstants.IGNORE:
				onIgnore(dialogView, data);
				break;
			case DialogResultConstants.NULL:
			default:
				throw new RuntimeException("Dialog result code is invalid: '" + resultValue + "'");
		}
	}

	public void onIgnore(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onAbort(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onCancel(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onConfirm(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onFalse(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onNo(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onNull(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onRetry(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onTrue(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onYes(DialogView dialogView, Object data) {
		// TODO Auto-generated method stub
		
	}

	public void onOk(DialogView dialogView, Object data) {
		
	}

}
