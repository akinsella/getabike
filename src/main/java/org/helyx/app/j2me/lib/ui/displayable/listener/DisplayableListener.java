package org.helyx.app.j2me.lib.ui.displayable.listener;

import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;


public interface DisplayableListener {

	void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next);
	void afterDisplayableSelection(AbstractDisplayable previous, AbstractDisplayable current);
	
}
