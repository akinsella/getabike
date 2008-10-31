package org.helyx.app.j2me.lib.ui.displayable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.lib.cache.Cache;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.callback.BasicReturnCallback;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.displayable.listener.DisplayableListener;
import org.helyx.app.j2me.lib.ui.theme.Theme;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.lib.ui.view.transition.IViewTransition;

public abstract class AbstractDisplayable implements DisplayableListener, CommandListener {

	private static final Log log = LogFactory.getLog("ABSTRACT_DISPLAYABLE");
	
	public AbstractDisplayable() {
		super();
	}
	
	private AbstractMIDlet midlet;
	
	private IReturnCallback returnCallback;

	public AbstractDisplayable(AbstractMIDlet midlet) {
		super();
		this.midlet = midlet;
	}

	public abstract String getTitle();
	
	public void setReturnCallback(IReturnCallback returnCallback) {
		this.returnCallback = returnCallback;
	}

	public IReturnCallback getReturnCallback() {
		return returnCallback;
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
	
	public void setPreviousDisplayable(AbstractDisplayable previousDisplayable) {
		setReturnCallback(new BasicReturnCallback(previousDisplayable));
	}

	public void setPreviousDisplayable(AbstractDisplayable previousDisplayable, IViewTransition canvasTransition) {
		setReturnCallback(new BasicReturnCallback(previousDisplayable, canvasTransition));
	}
	
	public boolean hasReturnCallback() {
		return returnCallback != null;
	}
	
	public void fireReturnCallback() {
		fireReturnCallback(null);
	}
	
	public void fireReturnCallback(Object data) {
		log.info("ReturnCallback Fired from: '" + this + "', Return callback: '" + returnCallback + "', data: '" + data + "'");
		if (returnCallback != null) {
			returnCallback.onReturn(this, data);
		}
	}

	private void showDisplayableInternal(final AbstractDisplayable srcDisplayable, final AbstractDisplayable targetDisplayable, final IViewTransition canvasTransition) {
//		Display.getDisplay(getMidlet()).callSerially(new Runnable() {
//
//			public void run() {
				if (srcDisplayable != null) {
					srcDisplayable.beforeDisplayableSelection(srcDisplayable, targetDisplayable);
				}
				targetDisplayable.beforeDisplayableSelection(srcDisplayable, targetDisplayable);
				changeDisplayable(srcDisplayable, targetDisplayable, canvasTransition);
				if (srcDisplayable != null) {
					srcDisplayable.afterDisplayableSelection(srcDisplayable, targetDisplayable);
				}
				targetDisplayable.afterDisplayableSelection(srcDisplayable, targetDisplayable);
//			}
//				
//		});
	}
	
	public abstract Displayable getDisplayable();
	
	protected void changeDisplayable(AbstractDisplayable srcDisplayable, AbstractDisplayable targetDisplayable, IViewTransition canvasTransition) {
		Displayable currentDisplayable = Display.getDisplay(getMidlet()).getCurrent();

		if (srcDisplayable != null) {
			srcDisplayable.onLeave();
		}
		if (currentDisplayable == null || currentDisplayable != targetDisplayable.getDisplayable()) {
			Display.getDisplay(getMidlet()).setCurrent(targetDisplayable.getDisplayable());
//			if (targetDisplayable.getDisplayable() instanceof Canvas) {
//				Canvas canvas = (Canvas)targetDisplayable.getDisplayable();
//				canvas.repaint();
//			}
		}
		targetDisplayable.onSelection();
	}
	
	protected void onSelection() {
		// TODO Auto-generated method stub
		
	}
	
	protected void onLeave() {
		// TODO Auto-generated method stub
		
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
		DialogUtil.showAlertMessage(this, title, message);			
	}
	
}
