package org.helyx.app.j2me.lib.ui.displayable.callback;

import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;

public class BasicReturnCallback implements IReturnCallback {

	private AbstractDisplayable previousDisplayable;
	private IViewTransition canvasTransition;
	
	public BasicReturnCallback(AbstractDisplayable previousDisplayable) {
		super();
		this.previousDisplayable = previousDisplayable;
	}

	public BasicReturnCallback(AbstractDisplayable previousDisplayable, IViewTransition canvasTransition) {
		super();
		this.previousDisplayable = previousDisplayable;
		this.canvasTransition = canvasTransition;
	}

	public AbstractDisplayable getPreviousDisplayable() {
		return previousDisplayable;
	}
	
	public IViewTransition getCanvasTransition() {
		return canvasTransition;
	}

	public void onReturn(AbstractDisplayable currentDisplayable) {
		if (canvasTransition != null) {
			currentDisplayable.showDisplayable(previousDisplayable, canvasTransition);
		}
		else {
			currentDisplayable.showDisplayable(previousDisplayable);
		}
	}
	
}
