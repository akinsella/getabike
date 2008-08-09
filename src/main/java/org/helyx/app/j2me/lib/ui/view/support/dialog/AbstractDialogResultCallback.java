package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;

public abstract class AbstractDialogResultCallback implements IReturnCallback, IDialogResultCallback {
	
	public AbstractDialogResultCallback() {
		super(); 
	}
	
	public void onReturn(Object data) {
		DialogView dialogView = (DialogView)data;
		onResult(dialogView);
	}

}
