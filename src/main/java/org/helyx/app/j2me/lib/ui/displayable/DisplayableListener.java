package org.helyx.app.j2me.lib.ui.displayable;

public interface DisplayableListener {

	void beforeDisplayableSelection(IAbstractDisplayable previous);
	void afterDisplayableSelection(IAbstractDisplayable previous);
	
}
