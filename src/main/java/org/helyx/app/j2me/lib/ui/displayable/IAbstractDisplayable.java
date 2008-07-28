package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;

public interface IAbstractDisplayable {

	boolean isFullScreenMode();
	
	String getTitle();
	
	void afterDisplayableSelection(IAbstractDisplayable previous);

	void beforeDisplayableSelection(IAbstractDisplayable previous);

	Displayable getDisplayable();
	
	AbstractMIDlet getMidlet();
	
	void setPreviousDisplayable(IAbstractDisplayable previousDisplayable);

	IAbstractDisplayable getPreviousDisplayable();

	void showDisplayable(IAbstractDisplayable displayable);

	void returnToPreviousDisplayable();

}
