package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.listener.DisplayableListener;
import org.helyx.app.j2me.lib.ui.displayable.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.displayable.transition.IViewTransition;
import org.helyx.app.j2me.lib.ui.theme.Theme;
import org.helyx.app.j2me.lib.ui.util.DialogUtil;

public abstract class AbstractDisplayable implements DisplayableListener, CommandListener {

	private static final String CAT = "ABSTRACT_DISPLAYABLE";
	
	public AbstractDisplayable() {
		super();
	}
	
	private AbstractMIDlet midlet;
	
	private AbstractDisplayable previousDisplayable;

	public AbstractDisplayable(AbstractMIDlet midlet) {
		super();
		this.midlet = midlet;
	}

	public abstract String getTitle();
	
	public void setPreviousDisplayable(AbstractDisplayable previousDisplayable) {
		this.previousDisplayable = previousDisplayable;
	}

	public AbstractDisplayable getPreviousDisplayable() {
		return previousDisplayable;
	}
	
	public void exit() {
		getMidlet().notifyDestroyed();
	}

	public void showDisplayable(Displayable displayable) {
		Display.getDisplay(midlet).setCurrent(displayable);
	}

	public void showDisplayable(AbstractDisplayable displayable, AbstractDisplayable previousDisplayable) {
		displayable.setPreviousDisplayable(previousDisplayable);
		showDisplayable(displayable);
	}

	public void show() {
		changeDisplayable(this, new BasicTransition());
		afterDisplayableSelection(null);
	}
	
	public Theme getTheme() {
		return midlet.getTheme();
	}
	
	public Locale getLocale() {
		return midlet.getLocale();
	}

	public void showDisplayable(AbstractDisplayable displayable) {
		beforeDisplayableSelection(this);
		changeDisplayable(displayable, new BasicTransition());
		afterDisplayableSelection(this);
	}

	public void showDisplayable(AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		beforeDisplayableSelection(this);
		changeDisplayable(targetDisplayable, canvasTransition);
		afterDisplayableSelection(this);
	}
	
	public abstract Displayable getDisplayable();
	
	protected void changeDisplayable(AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		Display.getDisplay(midlet).setCurrent(targetDisplayable.getDisplayable());
	}

	public void returnToPreviousDisplayable() {
		beforeDisplayableSelection(this);
		changeDisplayable(previousDisplayable, new BasicTransition());
		afterDisplayableSelection(this);
	}

	public AbstractMIDlet getMidlet() {
		return midlet;
	}

	public void afterDisplayableSelection(AbstractDisplayable previous) {
		
	}

	public void beforeDisplayableSelection(AbstractDisplayable previous) {
		
	}
	
	public void dispose() {
		
	}
	
	public void commandAction(Command command, Displayable displayable) {

	}
	
	public void showAlertMessage(String title, String message) {
		DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), title, message);			
	}

	public void showAlertMessage(String title, String message, int timeout) {
		DialogUtil.showAlertMessage(getMidlet(), getDisplayable(), title, message, timeout);			
	}
	
}
