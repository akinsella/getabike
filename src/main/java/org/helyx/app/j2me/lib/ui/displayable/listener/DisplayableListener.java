package org.helyx.app.j2me.lib.ui.displayable.listener;

import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;


public interface DisplayableListener {

	void beforeDisplayableSelection(AbstractDisplayable previous);
	void afterDisplayableSelection(AbstractDisplayable previous);
	
}
