/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.displayable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.cache.Cache;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.getabike.lib.renderer.text.ITextRenderer;
import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.BasicReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.getabike.lib.ui.displayable.listener.DisplayableListener;
import org.helyx.app.j2me.getabike.lib.ui.theme.Theme;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.BasicTransition;
import org.helyx.app.j2me.getabike.lib.ui.view.transition.IViewTransition;
import org.helyx.logging4me.Logger;


public abstract class AbstractDisplayable implements DisplayableListener, CommandListener {

	private static final Logger logger = Logger.getLogger("ABSTRACT_DISPLAYABLE");
	
	public AbstractDisplayable() {
		super();
	}
	
	private AbstractMIDlet midlet;
	
	private IReturnCallback returnCallback;
	
	private ITextRenderer titleRenderer;

	public AbstractDisplayable(AbstractMIDlet midlet) {
		super();
		this.midlet = midlet;
		init();
	}
	
	private void init() {
		titleRenderer = midlet.getI18NTextRenderer();
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
	
	public boolean containsMessage(String key) {
		return midlet.containsMessage(key);
	}

	public String getMessage(String key, Object object) {
		return midlet.getMessage(key, object);
	}
	
	public String getMessage(String key, Object[] objects) {
		return midlet.getMessage(key, objects);
	}
		
	public Theme getTheme() {
		return midlet.getTheme();
	}
	
	public Locale getLocale() {
		return midlet.getLocale();
	}
	
	
	public ITextRenderer getTitleRenderer() {
		return titleRenderer;
	}

	
	public void setTitleRenderer(ITextRenderer titleRenderer) {
		this.titleRenderer = titleRenderer;
	}

	public void show() {
		showDisplayableInternal(null, this, new BasicTransition());
	}
	
	public String renderTitle() {
		String title = getTitle();
		return titleRenderer != null ? titleRenderer.renderText(title) : title;
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
		if (logger.isDebugEnabled()) {
			logger.debug("ReturnCallback Fired from: '" + this + "', Return callback: '" + returnCallback + "', data: '" + data + "'");
		}
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
