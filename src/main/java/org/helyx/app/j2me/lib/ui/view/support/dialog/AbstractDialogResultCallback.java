package org.helyx.app.j2me.lib.ui.view.support.dialog;

import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;

public abstract class AbstractDialogResultCallback implements IReturnCallback, IDialogResultCallback {
	
	public AbstractDialogResultCallback() {
		super(); 
	}
	
	public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
		DialogView dialogView = (DialogView)currentDisplayable;
		onResult(dialogView, data);
	}

}
