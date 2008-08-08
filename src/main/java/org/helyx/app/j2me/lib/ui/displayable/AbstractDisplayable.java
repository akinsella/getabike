package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.lib.cache.Cache;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.i18n.ResourceBundle;
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
		getMidlet().exit();
	}
	
	public void pause() {
		getMidlet().pause();
	}
	
	public Cache getCache() {
		return midlet.getCache();
	}
	
	public ResourceBundle getResourceBundle() {
		return midlet.getResourceBundle();
	}
	
	public String getMessage(String key) {
		return midlet.getMessage(key);
	}
	
	public Theme getTheme() {
		return midlet.getTheme();
	}
	
	public Locale getLocale() {
		return midlet.getLocale();
	}

	public void show() {
		showDisplayableInternal(null, this, new BasicTransition());
	}
	
	public void showDisplayable(AbstractDisplayable targetDisplayable, AbstractDisplayable previousDisplayable) {
		showDisplayable(targetDisplayable, previousDisplayable, new BasicTransition());
	}
	
	public void showDisplayable(AbstractDisplayable targetDisplayable, AbstractDisplayable previousDisplayable, IViewTransition viewTransition) {
		targetDisplayable.setPreviousDisplayable(previousDisplayable);
		showDisplayableInternal(this, targetDisplayable, viewTransition);
	}

	public void showDisplayable(AbstractDisplayable targetDisplayable) {
		showDisplayableInternal(this, targetDisplayable, new BasicTransition());
	}

	public void showDisplayable(AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		showDisplayableInternal(this, targetDisplayable, canvasTransition);
	}

	public void returnToPreviousDisplayable() {
		showDisplayableInternal(this, previousDisplayable, new BasicTransition());
	}

	private void showDisplayableInternal(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		if (srcDisplayable != null) {
			srcDisplayable.beforeDisplayableSelection(srcDisplayable, targetDisplayable);
		}
		targetDisplayable.beforeDisplayableSelection(srcDisplayable, targetDisplayable);
		changeDisplayable(srcDisplayable, targetDisplayable, canvasTransition);
		if (srcDisplayable != null) {
			srcDisplayable.afterDisplayableSelection(srcDisplayable, targetDisplayable);
		}
		targetDisplayable.afterDisplayableSelection(srcDisplayable, targetDisplayable);
	}
	
	public abstract Displayable getDisplayable();
	
	protected void changeDisplayable(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		Displayable currentDisplayable = Display.getDisplay(getMidlet()).getCurrent();
		if (currentDisplayable == null || currentDisplayable != targetDisplayable.getDisplayable()) {
			Display.getDisplay(getMidlet()).setCurrent(targetDisplayable.getDisplayable());
		}
	}

	public AbstractMIDlet getMidlet() {
		return midlet;
	}

	public void beforeDisplayableSelection(AbstractDisplayable current, AbstractDisplayable next) {
		
	}
	
	public void afterDisplayableSelection(AbstractDisplayable previous, AbstractDisplayable current) {
		
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
