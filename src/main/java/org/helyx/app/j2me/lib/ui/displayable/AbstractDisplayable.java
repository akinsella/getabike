package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.ui.util.DialogUtil;

public abstract class AbstractDisplayable implements DisplayableListener, IAbstractDisplayable, CommandListener {
	
	private static final String CAT = "ABSTRACT_DISPLAYABLE";
	
	public AbstractDisplayable() {
		super();
	}
	
	private MIDlet midlet;
	
	private IAbstractDisplayable previousDisplayable;

	public AbstractDisplayable(MIDlet midlet) {
		super();
		this.midlet = midlet;
	}
	
	public void setPreviousDisplayable(IAbstractDisplayable previousDisplayable) {
		this.previousDisplayable = previousDisplayable;
	}

	public IAbstractDisplayable getPreviousDisplayable() {
		return previousDisplayable;
	}
	
	public void exit() {
		getMidlet().notifyDestroyed();
	}

	public void showDisplayable(Displayable displayable) {
		Display.getDisplay(midlet).setCurrent(displayable);
	}

	public void showDisplayable(IAbstractDisplayable displayable, IAbstractDisplayable previousDisplayable) {
		displayable.setPreviousDisplayable(previousDisplayable);
		showDisplayable(displayable);
	}

	public void show() {
		changeDisplayable(this, false, false);
		((IAbstractDisplayable)this).afterDisplayableSelection(null);
	}

	public void showDisplayable(IAbstractDisplayable displayable) {
		((IAbstractDisplayable)displayable).beforeDisplayableSelection(this);
		changeDisplayable(displayable, true, false);
		((IAbstractDisplayable)displayable).afterDisplayableSelection(this);
	}

	public void showDisplayable(IAbstractDisplayable displayable, boolean goBack) {
		((IAbstractDisplayable)displayable).beforeDisplayableSelection(this);
		changeDisplayable(displayable, true, goBack);
		((IAbstractDisplayable)displayable).afterDisplayableSelection(this);
	}

	public void showDisplayable(IAbstractDisplayable displayable, boolean doTranstion, boolean goBack) {
		((IAbstractDisplayable)displayable).beforeDisplayableSelection(this);
		changeDisplayable(displayable, doTranstion, goBack);
		((IAbstractDisplayable)displayable).afterDisplayableSelection(this);
	}
	
	protected void changeDisplayable(IAbstractDisplayable targetDisplayable, boolean doTransition, boolean goBack) {
		Display.getDisplay(midlet).setCurrent(targetDisplayable.getDisplayable());
	}

	public void returnToPreviousDisplayable() {
		((IAbstractDisplayable)previousDisplayable).beforeDisplayableSelection(this);
		changeDisplayable(previousDisplayable, true, true);
		((IAbstractDisplayable)previousDisplayable).afterDisplayableSelection(this);
	}

	public MIDlet getMidlet() {
		return midlet;
	}

	public void afterDisplayableSelection(IAbstractDisplayable previous) {
		
	}

	public void beforeDisplayableSelection(IAbstractDisplayable previous) {
		
	}
	
	public void dispose() {
		
	}
	
	public void showAlertMessage(String title, String message) {
		DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), title, message);			
	}

	public void showAlertMessage(String title, String message, int timeout) {
		DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), title, message, timeout);			
	}
	
	public void commandAction(Command command, Displayable displayable) {
	}

}
