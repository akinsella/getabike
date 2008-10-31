package org.helyx.app.j2me.lib.ui.displayable.callback;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;

public class BasicReturnCallback implements IReturnCallback {

	private Log log = LogFactory.getLog("BASIC_RETURN_CALLBACK");
	
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

	public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
		log.info("currentDisplayable: " + currentDisplayable);
		log.info("previousDisplayable: " + previousDisplayable);
		if (canvasTransition != null) {
			currentDisplayable.showDisplayable(previousDisplayable, canvasTransition);
		}
		else {
			currentDisplayable.showDisplayable(previousDisplayable);
		}
	}
	
}
